A simple Java PostgreSQL export GUI.
====================================

This software was a college project therefore is not intended to be a tool to generate advanced scripting database. 

Limitations:

- It only exports Table definitions.
- Primary Key columns with DEFAULT value from sequence is exported with DEFAULT value equal NULL.
- Column with custom data type is exported with varchar(255).
- Table DDLs is exported in random order, which may cause dependencies problem on script execution. Invidual execution of each DDL in right order solves this.

