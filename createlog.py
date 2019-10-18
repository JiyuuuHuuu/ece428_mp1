import random, string, sys

def random_func(lengthoflogword):
    return ''.join(random.choice(string.ascii_lowercase) for i in range(lengthoflogword))


def createlogfile(name, num):
    f = open(name, "w")
    start = "This is a log for vm" + str(num) + " - this is followed by random content"
    f.write(start)
    start = "Followed by some lines of sense - vm" + str(num) + " \n"
    for i in range(500):
        f.write(random_func(random.randint(1, 8)) + "\n")
        f.write(start)
    f.close()

createlogfile(sys.argv[1], sys.argv[2])
