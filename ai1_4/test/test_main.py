import unittest

from main import bfs

class MyTestCase(unittest.TestCase):

    def test_foo(self):
        self.assertEqual(1, bfs.foo())

    def test_endToEnd_normal_input_expect_normal_output(self):
        input = "1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15"
        expected_output = "RDLDDRR"
        actual_output = bfs.foo(input)
        self.assertEqual(expected_output, actual_output)


if __name__ == '__main__':
    unittest.main()
