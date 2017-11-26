#!/usr/bin/env python
from __future__ import print_function
import sys

def gcd(a, b):
    print("Euclid's Algorithm")
    print('a={0}, b={1}'.format(a, b))
    i = 1
    while b != 0:
        print('iteration {0}:'.format(i))
        if a <= b:
            print('a <= b -> b = b - a')
            b = b - a
        else:
            print('a > b -> a = a - b')
            a = a - b
        print('a={0}, b={1}'.format(a, b))
        i += 1
    return a


def unit_test():
    assert gcd(252, 105) == 21

if __name__ == '__main__':
    unit_test()
    if len(sys.argv) > 2:
        a = int(sys.argv[1], 10)
        b = int(sys.argv[2], 10)
        print('gcd({0},{1}) = {2}'.format(a, b, gcd(a, b)))
    else:
        print('Error expected two integer arguments')
