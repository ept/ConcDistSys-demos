from flask import Flask, request, render_template, jsonify

app = Flask(__name__)

tasks = []
next_task_id = 1

@app.route("/")
def index():
    return render_template("index.html")

@app.route("/tasks")
def list_tasks():
    return jsonify(tasks)

@app.route("/tasks", methods=["POST"])
def add_task():
    global tasks, next_task_id
    task = {"id": next_task_id, "text": request.form["text"], "done": False}
    next_task_id += 1
    tasks.append(task)
    return task
