import os
import sys

import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

from src.main.python.util.dataframe_util import clean_df
from src.main.python.util.file_util import list_directory_files


def generate_matched_graphic(path: str):
    # Uncomment to call from Java
    alignment = pd.read_csv(path)

    # Set a custom figure size
    plt.figure(figsize=(15, 6))
    plt.subplots_adjust(bottom=0.15)

    # Style for the line plot
    sns.set_theme(style="darkgrid")
    sns.set(font_scale=1.90)

    fig, axs = plt.subplots(2, 1, sharex='all', figsize=(15, 6))

    # Plot line plot using dataframe columns
    ax = sns.lineplot(ax=axs[0], data=alignment, label="PT", x="gap", y="%matched", marker='o')
    sns.lineplot(ax=axs[1], data=alignment, label="PT", x="gap", y="frechet", marker='o', alpha=0.5)

    ax.ticklabel_format(style='plain', axis='both')

    # Limit the x-axis size for better visualization
    ax.set_title("Alignment result")
    # ax.set(xlim=(alignment[pt_al_timestamp][0] - 4, alignment[pt_al_timestamp].max()))
    ax.set_xlabel("Gap")

    # Limit the y-axis size for better visualization
    # min = alignment[pt_al_distance].to_numpy()
    # ax.set(ylim=(np.amin(min[min > 0]) - 2, alignment[pt_al_distance].max() + 2))
    ax.set_ylabel("% matched points")

    ax.legend()

    # Show the graphic
    plt.savefig(path.replace(".csv", "") + ".pdf")
    plt.show()


if __name__ == "__main__":
    # 1.- Path to output csv files
    resources_path = os.path.join(os.getcwd(), os.pardir) + "\\resources\\output\\lift\\"
    # 2.- List all interesting files
    files = [file for file in list_directory_files(resources_path, ".csv") if not("-" in file)]

    for f in files:
        generate_matched_graphic(resources_path + f)