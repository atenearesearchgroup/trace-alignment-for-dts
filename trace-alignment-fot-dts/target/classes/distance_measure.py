import os
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import matplotlib.ticker as mticker
import numpy as np
import similaritymeasures
from scipy.spatial import distance


if __name__ == "__main__":
    # Auxiliar strings
    pt_al = "PTal-"
    dt_al = "DTal-"

    # Path to output csv files
    resources_path = os.path.join(os.getcwd(), os.pardir) + "\\resources\\output\\nxj\\"

    # Parse csv file to pandas dataframe
    alignment = pd.read_csv(resources_path + "LegoCarSyntheticTraces-1lapcar2-1lapcar2-accel-PT-0.75.csv")
    alignment_copy = alignment.copy()
    for name, _ in alignment.iteritems():
        alignment[name].update(pd.to_numeric(alignment[name], errors='coerce').fillna(0))

    # Select the real parameters
    parameters = ["yPos", "xPos", "distance", "light", "angle", "speed"]

    # Initialize auxiliary arrays
    snapshots_dt = []
    snapshots_pt = []
    for _ in range(len(parameters)):
        snapshots_dt.append([])
        snapshots_pt.append([])

    for i in range(len(alignment_copy["operationApplied"])):
        if alignment_copy["operationApplied"][i] == "Match":
            for j in range(len(parameters)):
                snapshots_dt[j].append(alignment[dt_al + parameters[j]][i])
                snapshots_pt[j].append(alignment[pt_al + parameters[j]][i])

    total_pt = alignment_copy["PTor-timestamp"]
    total_dt = alignment_copy["DTor-timestamp"]
    print(f"Number of matched ({len(snapshots_dt[0])},{len(snapshots_pt[0])}) - Total ({len(total_dt[total_dt!=' '])}"
          f",{len(total_pt[total_pt!=' '])})")
    print(f"{len(snapshots_pt[0])/len(total_pt[total_pt!=' '])*100} % of PT matched")
    print(f"{len(snapshots_dt[0])/len(total_dt[total_dt!=' '])*100} % of DT matched")
    if snapshots_dt:
        snapshots_dt_output = np.zeros((len(snapshots_dt[0]), len(parameters)))
        snapshots_pt_output = np.zeros((len(snapshots_pt[0]), len(parameters)))
        for i in range(len(parameters)):
            snapshots_dt_output[:, i] = snapshots_dt[i]
            snapshots_pt_output[:, i] = snapshots_pt[i]

        frechet_euclidean = similaritymeasures.frechet_dist(snapshots_dt_output, snapshots_pt_output, 2)
        print(f"Fréchet distance using Euclidean distance: {frechet_euclidean}")
        frechet_manhattan = similaritymeasures.frechet_dist(snapshots_dt_output, snapshots_pt_output, 1)
        print(f"Fréchet distance using Manhattan distance: {frechet_manhattan}")

        manhattan = []
        euclidean = []
        for i in range(len(snapshots_dt_output)):
            manhattan.append(distance.cdist([snapshots_dt_output[i]], [snapshots_pt_output[i]], 'cityblock')[0])
            euclidean.append(distance.cdist([snapshots_dt_output[i]], [snapshots_pt_output[i]], 'euclidean')[0])

        print(f"Euclidean distance: {np.mean(euclidean)} StDev: {np.std(euclidean)}")
        print(f"Manhattan distance: {np.mean(manhattan)} StDev: {np.std(manhattan)}")
