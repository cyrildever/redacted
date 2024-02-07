import argparse


def main(args):
    pass


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "-b",
        "--both",
        action=argparse.BooleanOptionalAction,
        help="Add to use both dictionary and tag",
    )
    parser.add_argument(
        "-d",
        "--dictionary",
        help="The optional path to the dictionary of words to redact",
    )
    parser.add_argument(
        "-H", "--hash", help="The hash engine for the round function [default sha-256]"
    )
    parser.add_argument("-i", "--input", help="The path to the document to be redacted")
    parser.add_argument(
        "-k",
        "--key",
        help="The optional key for the FPE scheme (leave it empty to use default)",
    )
    parser.add_argument("-o", "--output", help="The name of the output file")
    parser.add_argument(
        "-r",
        "--rounds",
        help="The number of rounds for the Feistel cipher [default 10]",
    )
    parser.add_argument(
        "-t", "--tag", help="The optional tag that prefixes words to redact [default ~]"
    )
    parser.add_argument(
        "-x",
        "--expand",
        action=argparse.BooleanOptionalAction,
        help="Add to expand a redacted document",
    )
    args = parser.parse_args()

    main(args)
