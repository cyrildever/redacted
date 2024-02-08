import argparse
from feistel import FPECipher, Engine, is_available_engine, SHA_256


from redacted import (
    DEFAULT_TAG,
    file2Dictionary,
    Redactor,
    RedactorWithDictionary,
    RedactorWithTag,
)


DEFAULT_KEY = "d51e1d9a9b12cd88a1d232c1b8730a05c8a65d9706f30cdb8e08b9ed4c7b16a0"
DEFAULT_ROUNDS = 10


def main(args):
    if not args.input or not args.output:
        raise Exception("Input and output file paths are mandatory")
    if not args.tag and not args.dictionary:
        raise Exception("Use to set either a tag or a dictionary")
    tag = args.tag
    if args.both:
        if not args.dictionary:
            raise Exception(
                "Tag and dictionary must be set if you want to use them both"
            )
        elif not args.tag:
            print("WARN - Tag not set: default ~ will be used!")
            tag = DEFAULT_TAG

    hash_engine = Engine(args.hash)
    if not args.hash and not is_available_engine(hash_engine):
        print("WARN - Wrong hash engine: default SHA-256 will be used instead!")
        hash_engine = SHA_256
    key = args.key
    if not key:
        key = DEFAULT_KEY
    rounds = int(args.rounds) if args.rounds else 0
    if rounds < 2:
        print("WARN - Not enough rounds: default 10 will be used instead!")
        rounds = DEFAULT_ROUNDS

    msg = "Start redacting..."
    if args.expand:
        msg = "Start expanding..."
    print(f"INFO - {msg}")

    # Prepare processing
    if args.dictionary:
        dic = file2Dictionary(args.dictionary)

    cipher = FPECipher(hash_engine, key, rounds)
    if args.both:
        redactor = Redactor(dictionary=dic, tag=tag, cipher=cipher, both=True)
    elif not args.expand and not dic.is_empty():
        redactor = RedactorWithDictionary(dictionary=dic, cipher=cipher)
    else:
        redactor = RedactorWithTag(tag=tag, cipher=cipher)

    # Do process
    with open(args.input, "r") as inputfile, open(args.output, "w") as outputfile:
        for line in inputfile:
            if not args.expand:
                redacted_line = redactor.redact(line)
                outputfile.write(redacted_line + "\n")
            else:
                expanded_line = redactor.expand(line)
                outputfile.write(expanded_line + "\n")

    print("INFO - Process completed.")


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
