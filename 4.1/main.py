import tkinter as tk
from tkinter import ttk, messagebox
import re

# Задания и регулярки
tasks = [
    "1.1 {a,b,c}, хотя бы один a и один b",
    "1.2 Десятый от конца символ — 1 (всего ≥10 символов)",
    "1.3 Не более одной пары последовательных единиц",
    "2.a Все 00 до любой 11",
    "2.b Кол-во нулей кратно 5",
    "3.1 Нет подцепочки 101",
    "3.2 Поровну 0 и 1, и нет префикса с отклонением >2",
    "3.3 Нулей кратно 5 и единиц чётно",
    "4. Телефонные номера всех видов",
    "5. Заработная плата (разные форматы)"
]

regexes = [
    r"^(?=.*a)(?=.*b)[abc]*$",                    # 1.1
    r"^.*1[01]{9}$",                              # 1.2
    r"^(?!.*11.*11)[01]*$",                       # 1.3
    r"^(?!.*11.*00)[01]*$",                       # 2.a
    r"^(1*01*01*01*01*0)*1*$",                    # 2.b
    r"^(?!.*101)[01]*$",                          # 3.1
    r"^(?=(1*01*0)*1*$)(?=(0*10*1)*0*$)[01]*$",   # 3.2
    r"^(?=([^0]*0[^0]*0[^0]*0[^0]*0[^0]*0)*[^0]*$)(?=([^1]*1[^1]*1)*[^1]*$)[01]*$",  # 3.3
    r"^\+?[0-9\s\-\(\)]{7,20}$",                  # 4
    r"^\$?\d{1,3}(?:,\d{3})*(?:\.\d{1,2})?(?:\s*(?:[рRr](?:уб(?:лей)?)?\.?|руб(?:лей)?\.?))?(?:\s*в\s*(?:час|день|неделю|месяц|год))?$"  # 5
]

# Проверка на допустимые символы (для бинарных задач)
def valid_chars_for(index):
    if index in {1, 2, 3, 4, 5, 6, 7}:
        return {'0', '1'}
    elif index == 0:
        return {'a', 'b', 'c'}
    return None  # для прочих выражений

def check_regex():
    index = task_combo.current()
    user_input = entry.get().strip()

    if index == -1 or not user_input:
        messagebox.showwarning("Ошибка", "Выберите выражение и введите строку.")
        return

    valid_chars = valid_chars_for(index)
    if valid_chars is not None:
        if any(c not in valid_chars for c in user_input):
            messagebox.showerror("Ошибка", f"Недопустимые символы. Разрешены: {''.join(sorted(valid_chars))}")
            return

    pattern = regexes[index]
    try:
        if re.fullmatch(pattern, user_input):
            result_label.config(text="✅ Совпадает!", fg="green")
        else:
            result_label.config(text="❌ Не совпадает.", fg="red")
    except re.error as e:
        result_label.config(text=f"Ошибка регулярки: {e}", fg="red")

# Интерфейс
root = tk.Tk()
root.title("Проверка регулярных выражений")
root.geometry("650x300")

frame = ttk.Frame(root, padding=10)
frame.pack(fill=tk.BOTH, expand=True)

ttk.Label(frame, text="Выберите регулярное выражение:").pack(anchor=tk.W)
task_combo = ttk.Combobox(frame, values=tasks, state="readonly", width=100)
task_combo.pack(fill=tk.X)

ttk.Label(frame, text="Введите строку:").pack(anchor=tk.W, pady=(10, 0))
entry = ttk.Entry(frame)
entry.pack(fill=tk.X)

ttk.Button(frame, text="Проверить", command=check_regex).pack(pady=10)
result_label = tk.Label(frame, text="", font=("Arial", 16))
result_label.pack()

root.mainloop()
