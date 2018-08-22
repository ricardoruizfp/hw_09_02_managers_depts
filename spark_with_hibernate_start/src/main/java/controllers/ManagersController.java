package controllers;
import db.DBHelper;
import models.Department;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class ManagersController {


    public ManagersController() {
        setupEndpoints();
    }


    public void setupEndpoints() {
        //index
        get("/managers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Manager> managers = DBHelper.getAll(Manager.class);
            model.put("managers", managers);
            model.put("template", "templates/managers/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        //new
        get("/managers/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/managers/new.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
        //create
        post("/managers", (req, res)-> {
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));

            int depId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(depId, Department.class);
            int budget = Integer.parseInt(req.queryParams("budget"));
            Manager manager = new Manager(firstName, lastName, salary, department, budget);
            DBHelper.save(manager);
            res.redirect("/managers");
            return null;
        });
        //show
        get("/managers/:id", (req, res)->{
            Map<String, Object> model = new HashMap<>();
            int managerId = Integer.parseInt(req.params("id"));
            Manager manager = DBHelper.find(managerId, Manager.class);
            model.put("manager", manager);
            model.put("template", "templates/managers/show.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
        //edit
        get("/managers/:id/edit", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            int managerId = Integer.parseInt(req.params("id"));
            Manager manager = DBHelper.find(managerId, Manager.class);
            model.put("manager", manager);
            model.put("template", "templates/managers/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
        //update
        post("/managers/:id", (req,res)->{
            int managerId = Integer.parseInt(req.params("id"));
            Manager manager = DBHelper.find(managerId, Manager.class);
            int depId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(depId, Department.class);
            manager.setFirstName(req.queryParams("firstName"));
            manager.setLastName(req.queryParams("lastName"));
            manager.setDepartment(department);
            manager.setSalary(Integer.parseInt(req.queryParams("salary")));
            manager.setBudget(Double.parseDouble(req.queryParams("budget")));
            DBHelper.update(manager);
            res.redirect("/managers");
            return null;
        });
        //



    }
}


