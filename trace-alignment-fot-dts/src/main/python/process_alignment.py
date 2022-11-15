from pathlib import Path
import sys

path_root = Path(__file__).parents[3]
sys.path.append(str(path_root))

from src.main.python.distance_measure import measure_distance
from src.main.python.graphic_generator import generate_graphic
from src.main.python.util.csv_util import get_writer

if __name__ == "__main__":
    print(sys.argv[1:])
    path = str(sys.argv[3]).replace("\"", "\\")
    filename = str(sys.argv[1])
    param_of_interest = str(sys.argv[2])

    # path = "C:\\Users\\paula\\OneDrive - Universidad de Málaga\\2022 - " \
    #        "BLAST\\trace-alignment\\trace-alignment-for-dts\\trace-alignment-fot-dts\\src\\main\\resources\\output" \
    #        "\\lift\\"
    # filename = "Bajada_4_0_4Bajada_4_0_4_01-0.2.csv"
    # param_of_interest = "accel(m/s2)"

    percentage_matched, frechet, mean, std = measure_distance(path + filename, param_of_interest)
    generate_graphic(path + filename, param_of_interest)

    file_w, writer = get_writer(path + filename[:filename.find("-")] + ".csv", ",", 'a')
    writer.writerow([filename[filename.find("-")+1:filename.find(".csv")], percentage_matched, frechet, mean, std])
    file_w.close()
