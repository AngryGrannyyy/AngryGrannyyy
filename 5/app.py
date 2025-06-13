import tkinter as tk
import time
import threading
from tkinter import PhotoImage
import os

from virus import Virus
from immuneSystem import ImmuneSystem

class App:
    def __init__(self, root):
        self.root = root
        self.root.title("Симуляция заражения")
        self.canvas = tk.Canvas(root, width=500, height=400, bg='white')
        self.canvas.pack()

        self.scale_top = 50
        self.scale_bottom = 345
        self.scale_x1 = 140
        self.scale_x2 = 260

        self.immunity_rect = self.canvas.create_rectangle(self.scale_x1, self.scale_top, self.scale_x2,
                                                          self.scale_bottom, fill='#FFE4C4')
        self.virus_rect = self.canvas.create_rectangle(self.scale_x1, self.scale_bottom, self.scale_x2,
                                                       self.scale_bottom, fill='#006400')

        image_path = os.path.join(os.path.dirname(__file__), "human_min.png")
        self.overlay_image = PhotoImage(file=image_path)
        center_x = (self.scale_x1 + self.scale_x2) // 2
        center_y = (self.scale_top + self.scale_bottom) // 2
        self.canvas.create_image(center_x, center_y, image=self.overlay_image)

        self.start_button = tk.Button(root, text="Начать симуляцию", command=self.start_simulation)
        self.start_button.pack(pady=10)

        self.virus = Virus()
        self.immunity = ImmuneSystem()

        self.current_ratio = 0.0
        self.virus_strength_label = tk.Label(root, text="Сила вируса: —", font=("Arial", 10))
        self.virus_strength_label.place(x=280, y=80)

        self.immunity_strength_label = tk.Label(root, text="Сила иммунитета: —", font=("Arial", 10))
        self.immunity_strength_label.place(x=280, y=110)

        self.infection_ratio_label = tk.Label(root, text="Заражение: 0%", font=("Arial", 10))
        self.infection_ratio_label.place(x=280, y=140)


    def start_simulation(self):
        self.canvas.delete("all")

        self.current_ratio = 0.0
        self.infection_ratio_label.config(text="Заражение: 0%")
        self.immunity_strength_label.config(text = "Сила иммунитета: —")
        self.virus_strength_label.config(text="Сила вируса: —")

        self.immunity_rect = self.canvas.create_rectangle(self.scale_x1, self.scale_top, self.scale_x2,
                                                          self.scale_bottom, fill='#FFE4C4')
        self.virus_rect = self.canvas.create_rectangle(self.scale_x1, self.scale_bottom, self.scale_x2,
                                                       self.scale_bottom, fill='#006400')

        center_x = (self.scale_x1 + self.scale_x2) // 2
        center_y = (self.scale_top + self.scale_bottom) // 2
        self.canvas.create_image(center_x, center_y, image=self.overlay_image)

        self.update_scale_by_ratio(self.current_ratio)

        self.start_button.config(state="disabled")
        self.start_button.config(text="Обновить симуляцию")
        threading.Thread(target=self.simulate_process).start()

    def simulate_process(self):
        self.virus = Virus()
        self.immunity = ImmuneSystem()
        print("[Старт] Вирус пытается проникнуть...")
        self.animate_penetration_attempt()
        print("[Иммунитет] Активация иммунной системы...")
        time.sleep(1.5)
        self.immunity.activate()
        self.immunity_strength_label.config(text=f"Сила иммунитета: {self.immunity.strength}")
        print(f"[Иммунитет] Сила иммунитета: {self.immunity.strength}")
        self.start_battle()
        self.start_button.config(state="normal")

    def animate_penetration_attempt(self):
        virus_icon = self.canvas.create_oval(10, 109, 30, 129, fill='#006400')
        success = False

        while not success:
            for _ in range(10, 190, 5):
                self.canvas.move(virus_icon, 5, 0)
                self.root.update()
                time.sleep(0.02)

            success = self.virus.try_to_infect()

            if not success:
                print(f"[Вирус] Попытка неудачна. Шанс увеличен до {self.virus.penetration_chance}%")
                msg = self.canvas.create_text(200, 30, text="Попытка неудачна. Вирус стал сильнее...",
                                              fill="red", tag="msg", font=("Arial", 10, "bold"))
                self.root.update()
                time.sleep(1)
                self.canvas.move(virus_icon, -180, 0)
                self.canvas.delete("msg")
            else:
                print("[Вирус] Проникновение успешно!")
                msg_insect=self.canvas.create_text(200, 30, text="Вирус проник в организм!", fill="blue",
                                        font=("Arial", 10, "bold"))
                self.root.update()
                time.sleep(1)
                self.canvas.delete(msg_insect)


        self.canvas.delete(virus_icon)
        self.virus.set_strength()
        self.virus_strength_label.config(text=f"Сила вируса: {self.virus.strength}")
        self.current_ratio = 0.1
        self.update_scale_by_ratio(self.current_ratio)
        print(f"[Вирус] Сила вируса: {self.virus.strength}")

    def start_battle(self):
        virus_strength = self.virus.strength
        immune_strength = self.immunity.strength

        print("[Битва] Проникновение завершено. Вирус начинает атаковать...")
        print(f"[DEBUG] Начальная доля заражения: {self.current_ratio}")

        immunity_active = False
        activation_delay = 2.0
        virus_push = virus_strength / 100
        immune_push = immune_strength / 100
        battle_speed = 0.015

        start_time = time.time()

        while 0.0 < self.current_ratio < 1.0:
            elapsed = time.time() - start_time
            if not immunity_active and elapsed >= activation_delay:
                immunity_active = True
                print("[Иммунитет] Иммунная система активировалась!")

            if immunity_active:
                delta = (virus_push - immune_push) * battle_speed
            else:
                delta = virus_push * battle_speed

            self.current_ratio += delta
            self.current_ratio = max(0.0, min(1.0, self.current_ratio))

            self.update_scale_by_ratio(self.current_ratio)
            self.infection_ratio_label.config(text=f"Заражение: {int(self.current_ratio * 100)}%")

            self.root.update()
            time.sleep(0.05)

        self.show_result(self.current_ratio)

    def show_result(self, final_ratio):
        if final_ratio >= 1.0:
            message = "Организм заражён!"
            color = '#006400'
        elif final_ratio <= 0.0:
            message = "Иммунитет победил!"
            color = "blue"

        print(f"[Результат] {message}")
        self.canvas.create_text(200, 380, text=message, fill=color, font=("Arial", 14, "bold"))
        self.canvas.create_text(200, 380, text=message, fill=color, font=("Arial", 14, "bold"))

    def update_scale_by_ratio(self, virus_ratio):
        virus_height = int((self.scale_bottom - self.scale_top) * virus_ratio)
        immune_height = (self.scale_bottom - self.scale_top) - virus_height

        self.canvas.coords(
            self.immunity_rect,
            self.scale_x1,
            self.scale_top,
            self.scale_x2,
            self.scale_top + immune_height
        )
        self.canvas.coords(
            self.virus_rect,
            self.scale_x1,
            self.scale_top + immune_height,
            self.scale_x2,
            self.scale_bottom
        )

if __name__ == "__main__":
    root = tk.Tk()
    app = App(root)
    root.mainloop()
