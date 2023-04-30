DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS project_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS project;

CREATE TABLE IF NOT EXISTS project
(
    project_id INT AUTO_INCREMENT NOT NULL,
    project_name VARCHAR(128) NOT NULL,
    estimated_hours DECIMAL(7,2),
    actual_hours DECIMAL(7,2),
    difficulty INT,
    notes TEXT,
    PRIMARY KEY (project_id)
);
CREATE TABLE IF NOT EXISTS category
(
    category_id INT AUTO_INCREMENT NOT NULL,
    category_name VARCHAR(128) NOT NULL,
    PRIMARY KEY (category_id)
);
CREATE TABLE IF NOT EXISTS material
(
    material_id INT AUTO_INCREMENT NOT NULL,
    project_id INT,
        FOREIGN KEY(project_id) REFERENCES project(project_id)
        ON DELETE CASCADE,
    material_name VARCHAR(128) NOT NULL,
    num_required INT,
    cost DECIMAL(7,2),
    PRIMARY KEY (material_id)
);
CREATE TABLE IF NOT EXISTS step
(
    step_id INT NOT NULL,
    project_id INT,
        FOREIGN KEY(project_id) REFERENCES project(project_id)
        ON DELETE CASCADE,
    step_text TEXT NOT NULL,
    step_order INT NOT NULL,
    PRIMARY KEY (step_id)
);
CREATE TABLE IF NOT EXISTS project_category
(
    project_id INT,
    category_id INT,
    CONSTRAINT fkey_project
        FOREIGN KEY(project_id) REFERENCES project(project_id),
    CONSTRAINT fkey_category
        FOREIGN KEY(category_id) REFERENCES category(category_id),
    CONSTRAINT pkey_combination
        PRIMARY KEY (project_id, category_id)
);