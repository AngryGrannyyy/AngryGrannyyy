import tkinter as tk
from tkinter import ttk, messagebox
import re

tasks = [
    "1. (*)01* — любая последовательность нулей перед единицами",
    "2. (0+1)01 — либо 0, либо 1, затем 0 и 1",
    "3. 00(0+1)* — начинается с 00, потом любые нули и единицы"
]

regexes = [
    r"^0*1*$",           # (*)01*
    r"^(0|1)01$",        # (0+1)01
    r"^00[01]*$",        # 00(0+1)*
]

valid_chars = {'0', '1'}

def check_regex():
    index = task_combo.current()
    user_input = entry.get().strip()

    if index == -1 or not user_input:
        messagebox.showwarning("Ошибка", "Выберите выражение и введите строку.")
        return

    if any(c not in valid_chars for c in user_input):
        messagebox.showerror("Ошибка", "Строка содержит недопустимые символы (только 0 и 1 разрешены).")
        return

    pattern = regexes[index]
    if re.fullmatch(pattern, user_input):
        result_label.config(text="✅ Совпадает!", fg="green")
    else:
        result_label.config(text="❌ Не совпадает.", fg="red")


root = tk.Tk()
root.title("Проверка бинарных выражений")
root.geometry("650x300")

frame = ttk.Frame(root, padding=10)
frame.pack(fill=tk.BOTH, expand=True)

ttk.Label(frame, text="Выберите регулярное выражение:").pack(anchor=tk.W)
task_combo = ttk.Combobox(frame, values=tasks, state="readonly", width=100)
task_combo.pack(fill=tk.X)

ttk.Label(frame, text="Введите строку (только 0 и 1):").pack(anchor=tk.W, pady=(10, 0))
entry = ttk.Entry(frame)
entry.pack(fill=tk.X)

ttk.Button(frame, text="Проверить", command=check_regex).pack(pady=10)
result_label = tk.Label(frame, text="", font=("Arial", 16))
result_label.pack()

root.mainloop()
