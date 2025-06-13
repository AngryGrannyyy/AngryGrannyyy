import random

class Virus:
    def __init__(self):
        self.penetration_chance = 20
        self.strength = 0

    def try_to_infect(self):
        success = random.randint(1, 100) <= self.penetration_chance
        if not success:
            self.penetration_chance += 10
        return success

    def set_strength(self):
        self.strength = random.randint(30, 100)
