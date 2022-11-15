import os

from src.main.python.util import csv_util, file_util


def _add_tolerances(path: str, delim: str, position: int):
    file_r, reader = csv_util.get_reader(path, delim)
    input_matrix = list(reader)
    output = input_matrix[0:position]
    output.append([0.1, 0.1, 0.1, 0.1, 0.05, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1])
    output.extend(input_matrix[position:])

    file_r.close()

    file_w, writer = csv_util.get_writer(path, delim, 'w')
    writer.writerows(output)
    file_w.close()


def _preprocess_file(in_path, input_filename, out_path, pattern, substring, delim, position):
    # Processed files will be csv instead of txt
    output_filename = input_filename.replace(".txt", ".csv")

    # Replace strings and process the timestamps
    file_util.replace_str(in_path + input_filename, out_path + output_filename, pattern, substring)
    _add_tolerances(out_path + output_filename, delim, 2)
    csv_util.process_timestamp(out_path + output_filename, delim, position)


if __name__ == "__main__":
    # Project paths to csv files
    project_path = os.path.join(os.getcwd(), os.pardir)
    input_path = project_path + "\\resources\\input\\lift\\"

    # Lift files
    raw_path = input_path + "01-raw\\"

    # List all the files in the input directory
    filenames = file_util.list_directory_files(raw_path, ".txt")
    # Create a directory for the processed files
    output_processed_path = input_path + "02-processed_csv\\"
    if not os.path.isdir(output_processed_path):
        os.makedirs(output_processed_path)

    for filename in filenames:
        _preprocess_file(raw_path, filename, output_processed_path, "\t", ",", ",", 1)
