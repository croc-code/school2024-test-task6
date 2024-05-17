from typing import Iterator, Optional
from collections import defaultdict


class ImbalanceSensor:
    def __init__(self, report_path: str = ""):
        """
        Initialize ImbalanceSensor with an optional report_path parameter.
        If report_path is not provided, default to "report.txt".
        """
        self.report_path: str = "report.txt" if not report_path else report_path

    def get_result(self, result_path: str = "") -> None:
        """
        Analyze the report data and generate the result based on the imbalance calculation.
        Write the result to the specified result_path file or default to "result.txt".
        """
        # Ensure result file is created even if there's no standard or incorrect data
        with open(result_path, "w") as f:
            standard = self.get_standard()
            if standard is None:
                return

        statistics = defaultdict(lambda: self.get_standard())

        for row in self.get_report():
            if row:
                row = row.split()
                worker = (row[0], f"{row[1]} {row[2]} {row[3]}")
                statistics[worker] -= float(row[-1])

        # Sort workers alphabetically by name and imbalance sign
        sorted_workers = sorted(statistics.keys(), key=lambda w: (statistics[w] < 0, w[1]))

        with open(result_path, "w") as f:
            for worker in sorted_workers:
                imbalance = abs(statistics[worker]) / standard * 100
                if imbalance > 10:
                    name = self.abbreviate_name(worker[1])
                    value = -1 * statistics[worker]
                    sign, value = "+" if value > 0 else "-", abs(value)

                    if value.is_integer():
                        f.write(f"{name} {sign}{int(value)}\n")
                    else:
                        f.write(f"{name} {sign}{value}\n")

    def get_standard(self) -> Optional[float]:
        """
        Retrieve the standard value from the report file.
        Return None if the file is not found or the standard value is not in the correct format.
        """
        try:
            with open(self.report_path, "r") as f:
                standard = float(f.readline().strip())
                return standard
        except FileNotFoundError:
            print("File not found.")
        except ValueError:
            print("Invalid format for standard value.")
        return None

    def get_report(self) -> Iterator[Optional[str]]:
        """
        Iterate over the lines of the report file and yield each line, stripping any extra whitespace.
        Skip lines that do not have the correct format (not exactly 6 elements).
        """
        try:
            with open(self.report_path, "r") as f:
                f.readline()  # Skip the standard line
                for line in f:
                    if len(line.split()) != 6:
                        yield None
                    else:
                        yield line.strip()
        except FileNotFoundError:
            print("File not found.")
            return iter([])  # Return an empty iterator

    @staticmethod
    def abbreviate_name(full_name: str) -> str:
        """
        Abbreviate a full name into the format "Lastname F.I.".
        Raise a ValueError if the full name does not consist of three parts.
        """
        parts = full_name.split()
        if len(parts) == 3:
            return f"{parts[0]} {parts[1][0]}.{parts[2][0]}."
        raise ValueError("The full name must consist of three parts: last name, first name, and patronymic.")


if __name__ == "__main__":
    imb = ImbalanceSensor()
    imb.get_result()
