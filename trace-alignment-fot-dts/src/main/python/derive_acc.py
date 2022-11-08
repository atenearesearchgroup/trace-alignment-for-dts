import os

import pandas
import pandas as pd

from src.main.python.util import file_util


def derive_speed_and_position(time, accel):
    speed = [0]
    position = [0]
    new_accel = [(accel[0] - 1) * 9.81]

    for i in range(1, len(time)):
        time_inc = time[i] - time[i - 1]
        new_accel.append((accel[i] - 1) * 9.81)
        speed.append(speed[-1] + new_accel[i] * time_inc)

    for i in range(1, len(time)):
        time_inc = time[i] - time[i - 1]
        speed[i] += (0 - speed[-1]) * (time[i] - time[0]) / (time[len(time) - 1] - time[0])
        position.append(position[-1] + speed[i - 1] * time_inc
                        + (1 / 2 * new_accel[i] * (time_inc ** 2)))

    for i in range(1, len(time)):
        position[i] += (0 - position[-1]) * (time[i] - time[0]) / (time[len(time) - 1] - time[0])

    return speed, position, new_accel


if __name__ == "__main__":
    # Project paths to csv files
    project_path = os.path.join(os.getcwd(), os.pardir)
    input_path = project_path + "\\resources\\input"

    # Lift files
    lift_path = input_path + "\\lift\\processed_csv\\"

    # Create output directory
    out_lift_path = input_path + "\\lift\\derived_values\\"
    if not os.path.isdir(out_lift_path):
        os.makedirs(out_lift_path)

    # List all the files in the input directory
    filenames = file_util.list_directory_files(lift_path, ".csv")

    for filename in filenames:
        # Parse csv file to pandas dataframe
        alignment = pd.read_csv(lift_path + filename, delimiter=";")

        time_data = alignment.loc[:, "Time(s)"]
        acceleration_data = alignment.loc[:, "az(g)"]
        speed_data, position_data, accel_data = derive_speed_and_position(time_data, acceleration_data)

        output_df = pandas.DataFrame()
        output_df.insert(0, "timestamp(s)", time_data, True)
        output_df.insert(0, "speed(m/s)", speed_data, True)
        output_df.insert(0, "position(m)", position_data, True)
        output_df.insert(0, "accel(m/s2)", accel_data, True)

        output_df.to_csv(out_lift_path + filename, index=False)
