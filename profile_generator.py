#  Copyright (c) 2023. Knight Hat
#  All rights reserved.
#  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
#
#  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
#
#  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

from json import dumps
from random import randrange, choice
from string import ascii_letters
from sys import argv
from uuid import uuid4

fonts = ["Arial", "Times New Roman", "Stardos Stencil"]
weights = ["plain", "bold", "italic", "bold|italic"]


def uuid() -> str:
    return str(uuid4())


def randcolor() -> list[int]:
    return [randrange(256), randrange(256), randrange(256)]


def randstr(r: int) -> str:
    return ''.join(choice(ascii_letters) for _ in range(r))


class Font:
    def __init__(self):
        self.name = choice(fonts)
        self.weight = choice(weights)
        self.size = randrange(15) + 1


class BLabel:
    def __init__(self):
        self.text = randstr(5)
        self.foreground = randcolor()
        self.font = Font()


class Button:
    def __init__(self, x: int, y: int):
        self.uuid = uuid()
        self.x = x
        self.y = y
        self.task = None
        self.icon = {"background": randcolor(), "border": randcolor()}
        self.label = BLabel()


class Profile:
    def __init__(self):
        self.uuid = uuid()
        self.displayName = randstr(10)
        self.default = False
        self.rows = randrange(4) + 1
        self.columns = randrange(6) + 1
        self.gap = randrange(10)
        self.buttons: list[Button] = []

        for y in range(self.rows):
            for x in range(self.columns):
                self.buttons.append(Button(x, y))

    def __json__(self) -> str:
        return dumps(self, default=lambda btn: btn.__dict__, indent=2)


if __name__ == '__main__':
    times = 1 if len(argv) == 1 else int(argv[1])
    for _ in range(times):
        profile = Profile()
        with open(f'{profile.uuid}.profile', 'w') as p:
            p.write(profile.__json__())
