import os

from src.main.python.util import csv_util, file_util


def _preprocess_file(in_path, input_filename, out_path, pattern, substring, delim, position):
    # Processed files will be csv instead of txt
    output_filename = input_filename.replace(".txt", ".csv")

    # Replace strings and process the timestamps
    file_util.replace_str(in_path + input_filename, out_path + output_filename, pattern, substring)
    csv_util.process_timestamp(out_path + output_filename, delim, position)


if __name__ == "__main__":
    # Project paths to csv files
    project_path = os.path.join(os.getcwd(), os.pardir)
    input_path = project_path + "\\resources\\input"

    # Lift files
    lift_path = input_path + "\\lift\\"

    # List all the files in the input directory
    filenames = file_util.list_directory_files(lift_path, ".txt")
    # Create a directory for the processed files
    output_processed_path = lift_path + "processed_csv\\"
    if not os.path.isdir(output_processed_path):
        os.makedirs(output_processed_path)

    for filename in filenames:
        _preprocess_file(lift_path, filename, output_processed_path, "\t", ";", ";", 1)
