import random

class ImmuneSystem:
    def __init__(self):
        self.strength = 0
        self.active = False

    def activate(self):
        self.active = True
        self.strength = random.randint(30, 100)
