from json import dumps
from random import randrange, randint, choice
from string import ascii_letters
from sys import argv
from uuid import uuid4


def randcolor() -> list[int]:
    return [randrange(256), randrange(256), randrange(256)]


def randstr(r: int) -> str:
    return ''.join(choice(ascii_letters) for i in range(r))


def uuidstr() -> str:
    return str(uuid4())


def button(x: int, y: int) -> dict:
    return {
        "uuid": uuidstr(),
        "x": x,
        "y": y,
        "icon": {
            "outer": randcolor(),
            "inner": randcolor()
        },
        "label": {
            "text": randstr(5),
            "color": randcolor(),
            "font": {
                "name": "Stardos Stencil",
                "weight": "plain",
                "size": 14
            }
        }
    }


def generate() -> dict:
    uuid = uuidstr()

    rows = randint(1, 4)
    columns = randint(1, 6)

    buttons = []

    for y in range(rows):
        for x in range(columns):
            buttons.append(button(x, y))

    return {
        "uuid": uuid,
        "displayName": randstr(10),
        "default": False,
        "rows": rows,
        "columns": columns,
        "gap": randrange(4),
        "buttons": buttons
    }


if __name__ == '__main__':
    times = 1 if len(argv) == 1 else int(argv[1])
    for i in range(times):
        jsonObject = generate()
        encodedJson = dumps(jsonObject, indent=2)
        with open(f'{jsonObject["uuid"]}.profile', 'w') as f:
            f.write(encodedJson)
