import unittest
from test_data import test_data
from ImbalanceSensor import ImbalanceSensor
import os


class TestImbalanceSensor(unittest.TestCase):

    def test_get_result(self):
        for input_data, expected_output in test_data:
            # Create a real file with input_data
            with open("report.txt", "w") as f:
                f.write("\n".join(input_data))

            # Create an instance of ImbalanceSensor
            sensor = ImbalanceSensor("report.txt")

            # Call get_result method
            sensor.get_result()

            # Check if result.txt exists and compare its content
            self.assertTrue(os.path.exists("result.txt"))
            with open("result.txt", "r") as result_file:
                result = result_file.read()
                self.assertEqual(result, "\n".join(expected_output) + "\n")

            # Remove temporary files
            os.remove("report.txt")
            os.remove("result.txt")


if __name__ == "__main__":
    unittest.main()
