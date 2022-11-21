import os
import sys

import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

from src.main.python.util.dataframe_util import clean_df


def generate_graphic(path: str, parameter_of_interest: str):
    # Uncomment to call from Java
    alignment = pd.read_csv(path)

    # Columns of interest
    pt_or_timestamp = "PTor-timestamp"
    pt_or_interest = "PTor-" + parameter_of_interest
    dt_or_timestamp = "DTor-timestamp"
    dt_or_interest = "DTor-" + parameter_of_interest

    pt_al_timestamp = "PTal-timestamp"
    pt_al_interest = "PTal-" + parameter_of_interest
    dt_al_timestamp = "DTal-timestamp"
    dt_al_interest = "DTal-" + parameter_of_interest

    # Separate and clean the dataframe for plotting
    selected_pt = clean_df(alignment, [pt_or_timestamp, pt_or_interest])
    selected_dt = clean_df(alignment, [dt_or_timestamp, dt_or_interest])

    # Set a custom figure size
    plt.figure(figsize=(15, 6))
    plt.subplots_adjust(bottom=0.15)

    # Style for the line plot
    sns.set_theme(style="darkgrid")
    sns.set(font_scale=1.90)

    # Plot line plot using dataframe columns
    # Physical Twin trajectory
    # selected_pt[pt_or_interest] = selected_pt[pt_or_interest].apply(lambda x: x - 3)
    sns.lineplot(data=selected_pt, label="PT", x=pt_or_timestamp, y=pt_or_interest,
                 marker='o')
    # Digital Twin trajectory
    ax = sns.lineplot(data=selected_dt, label="DT", x=dt_or_timestamp, y=dt_or_interest,
                      marker='o', alpha=0.5)

    # Plot alignment with matching points
    for i in range(len(alignment["operationApplied"])):
        if alignment["operationApplied"][i] == "Match":
            ax.plot([float(alignment[pt_al_timestamp][i]), float(alignment[dt_al_timestamp][i])],
                    [float(alignment[pt_al_interest][i]), float(alignment[dt_al_interest][i])], color='black', ls=':',
                    zorder=0)

    ax.ticklabel_format(style='plain', axis='both')

    # Limit the x-axis size for better visualization
    ax.set_title("Trace alignment PT against DT")
    # ax.set(xlim=(alignment[pt_al_timestamp][0] - 4, alignment[pt_al_timestamp].max()))
    ax.set_xlabel("POSIX Timestamp (seconds)")

    # Limit the y-axis size for better visualization
    # min = alignment[pt_al_distance].to_numpy()
    # ax.set(ylim=(np.amin(min[min > 0]) - 2, alignment[pt_al_distance].max() + 2))
    # ax.set_ylabel("Servo 4 angles (degrees)")

    ax.legend()

    # Show the graphic
    plt.savefig(path.replace(".csv", "") + ".pdf")
    # plt.show()


if __name__ == "__main__":
    # 1.- Path to output csv files
    resources_path = os.path.join(os.getcwd(), os.pardir) + "\\resources\\output\\lift\\"
    # 2.- Name of the input aligned file
    file_path = resources_path + "Bajada_4_0_4Bajada_4_0_4_01-0.2.csv"
    # 3.- Dimension to plot against time
    param_of_interest = "accel(m/s2)"

    generate_graphic(file_path, param_of_interest)
