import os

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns

if __name__ == "__main__":
    # Path to output csv files
    resources_path = os.path.join(os.getcwd(), os.pardir) + "\\resources\\output\\lift\\"

    # Parse csv file to pandas dataframe
    alignment = pd.read_csv(resources_path + "bajarSubir4plantas_dt2bajarSubir4plantas_pt-0.05.csv")
    alignment_copy = alignment.copy()
    for name, _ in alignment.iteritems():
        alignment[name].update(pd.to_numeric(alignment[name], errors='coerce').fillna(0))

    # Set a custom figure size
    plt.figure(figsize=(15, 6))
    plt.subplots_adjust(bottom=0.15)

    # Style for the line plot
    sns.set_theme(style="darkgrid")
    sns.set(font_scale=1.90)

    # Plot line plot using dataframe columns
    # Physical Twin trajectory
    sns.lineplot(data=alignment, label="PT", x="PTor-timestamp", y="PTor-acceleration",
                 marker='o')
    # Digital Twin trajectory
    ax = sns.lineplot(data=alignment, label="DT", x="DTor-timestamp", y="DTor-acceleration",
                      marker='o', alpha=0.5)

    # Plot alignment with matching points
    snapshots_a_x = []
    snapshots_a_y = []
    snapshots_b_x = []
    snapshots_b_y = []
    pt_al_timestamp = "PTal-timestamp"
    pt_al_distance = "PTal-acceleration"
    dt_al_timestamp = "DTal-timestamp"
    dt_al_distance = "DTal-acceleration"
    for i in range(len(alignment_copy["operationApplied"])):
        if alignment_copy["operationApplied"][i] == "Match":
            ax.plot([alignment[pt_al_timestamp][i], alignment[dt_al_timestamp][i]],
                    [alignment[pt_al_distance][i], alignment[dt_al_distance][i]], color='black', ls=':',
                    zorder=0)
            snapshots_a_x.append(alignment[pt_al_timestamp][i])
            snapshots_a_y.append(alignment[pt_al_distance][i])
            snapshots_b_x.append(alignment[dt_al_timestamp][i])
            snapshots_b_y.append(alignment[dt_al_distance][i])

    ax.ticklabel_format(style='plain', axis='both')

    # Limit the x-axis size for better visualization
    ax.set_title("Trace alignment PT against DT")
    #ax.set(xlim=(alignment[pt_al_timestamp][0] - 4, alignment[pt_al_timestamp].max()))
    ax.set_xlabel("POSIX Timestamp (seconds)")

    # Limit the y-axis size for better visualization
    # min = alignment[pt_al_distance].to_numpy()
    # ax.set(ylim=(np.amin(min[min > 0]) - 2, alignment[pt_al_distance].max() + 2))
    # ax.set_ylabel("Servo 4 angles (degrees)")

    ax.legend()

    # Show the graphic
    plt.savefig(resources_path + 'output.pdf')
    plt.show()
