package se.lexicon.leo.data;

import se.lexicon.leo.db.MyConnection;
import se.lexicon.leo.model.Person;
import se.lexicon.leo.model.Todo;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class TodoItemsDAOIMPL implements TodoItemsDAO {

    private static Collection<Todo> todoList = new ArrayList<>();

    @Override
    public Todo create(Todo todo) {
        String createTodo = "INSERT INTO todo_item (title, description, deadline, done, assignee_id) VALUES ( ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = MyConnection.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(createTodo);
            preparedStatement.setString(1, todo.getTitle());
            preparedStatement.setString(2, todo.getDescription());
            preparedStatement.setString(3, todo.getDeadline().toString());
            preparedStatement.setBoolean(4, todo.isDone()); //Ask Simon how to get or if needs the done
            preparedStatement.setInt(5, todo.assignee.getPERSONID());
            preparedStatement.executeUpdate(createTodo);

            rs = preparedStatement.getResultSet();

            todo = new Todo(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getDate(4).toLocalDate(), rs.getBoolean(5), null);
            return todo;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todo;
    }

    @Override
    public Collection<Todo> findAll() {

        Collection<Todo> foundTodos = new ArrayList<>();
        String findAll = "SELECT*FROM todo_item";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = MyConnection.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(findAll);

            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Todo todo = new Todo(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getDate(4).toLocalDate(), rs.getBoolean(5), null);
                foundTodos.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundTodos;
    }

    @Override
    public Todo findById(int todoId) {
        String findByID = "SELECT*FROM todo_item WHERE todo_id = ?";
        Todo todo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = MyConnection.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(findByID);
            preparedStatement.setInt(1, todoId);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                todo = new Todo(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getDate(4).toLocalDate(), rs.getBoolean(5), null);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return todo;
    }

    @Override
    public Collection<Todo> findByDoneStatus(boolean doneStatus) {
        Collection<Todo> doneTodos = new ArrayList<>();
        String findDoneStatus = "SELECT*FROM todo_item WHERE done = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = MyConnection.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(findDoneStatus);
            preparedStatement.setBoolean(1, doneStatus);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Todo todo = new Todo(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getDate(4).toLocalDate(), rs.getBoolean(5), null);
                doneTodos.add(todo);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return doneTodos;
    }

    @Override
    public Collection<Todo> findByAssignee(int personId) {

        Collection<Todo> assigneeIdMatches = new ArrayList<>();
        String findByAssigneeId = "SELECT*FROM todo_item WHERE assignee_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = MyConnection.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(findByAssigneeId);
            preparedStatement.setInt(1, personId);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Todo todo = new Todo(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getDate(4).toLocalDate(), rs.getBoolean(5), null);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return assigneeIdMatches;
    }

    @Override
    public Collection<Todo> findByAssignee(Person assignee) {

        Collection<Todo> assigneeMatches = new ArrayList<>();
        String findByAssigneeObject = "SELECT*FROM todo_item WHERE assignee_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = MyConnection.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(findByAssigneeObject);
            preparedStatement.setObject(1, assignee.getPERSONID());
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Todo todo = new Todo(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getDate(4).toLocalDate(), rs.getBoolean(5), null);
                assigneeMatches.add(todo);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return assigneeMatches;
    }

    @Override
    public Collection<Todo> findUnassignedTodoItems() {

        Collection<Todo> unassignedTodo = new ArrayList<>();
        String findUnassignedTodos = "SELECT*FROM todo_item WHERE assignee_id Is null";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = MyConnection.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(findUnassignedTodos);

            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Todo todo = new Todo(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getDate(4).toLocalDate(), rs.getBoolean(5), null);
                unassignedTodo.add(todo);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return unassignedTodo;
    }

    @Override
    public Todo update(Todo todo) {


        int numberOfRowsAffected = 0;
        String updateTodo = "UPDATE todo_item SET title = ?, description = ?, deadline = ?, done = ?, assignee_id = ? WHERE todo_id = ? ";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MyConnection.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(updateTodo);
            preparedStatement.setString(1, todo.getTitle());
            preparedStatement.setString(2, todo.getDescription());
            preparedStatement.setString(3, todo.getDeadline().toString());
            preparedStatement.setBoolean(4, todo.isDone());
            preparedStatement.setInt(5, todo.getAssignee().getPERSONID());
            preparedStatement.setInt(6, todo.getTODOID());
            numberOfRowsAffected = preparedStatement.executeUpdate();

            if(numberOfRowsAffected >= 1) {
                return todo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int todoId) {

        String deleteTodoByID = "DELETE FROM todo_item WHERE todo_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MyConnection.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(deleteTodoByID);
            preparedStatement.setInt(1, todoId);
            int deletedRows = preparedStatement.executeUpdate();

            if (deletedRows == 1) {
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }


}
