import sys

def printin(isOK, nameTest):
    if isOK:
        print(nameTest + "... OK")
    else:
        sys.exit("In " + nameTest + " there was an error")