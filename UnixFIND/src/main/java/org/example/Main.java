package org.example;

import java.util.List;
import java.util.Arrays;
import java.util.List;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
    public class Main {

        public static void main(String[] args) {

            // Build directory tree
            FileSystemNode root = new FileSystemNode("root");

            FileSystemNode logs = new FileSystemNode("logs");
            FileSystemNode src = new FileSystemNode("src");

            root.addChild(logs);
            root.addChild(src);

            logs.addChild(new FileSystemNode(500, "app.log" ));
            logs.addChild(new FileSystemNode(1500, "error.log" ));

            src.addChild(new FileSystemNode(200, "Main.java" ));
            src.addChild(new FileSystemNode(300, "Utils.java" ));
            src.addChild(new FileSystemNode(100, "README.txt" ));

            // Build filters
//            List<Filter> filters = Arrays.asList(
//                    new ExtensionsFilter(".log")
////                    new SizeFilter(1800, SizeFilter.Operator.GT),
////                    new NameContainsFilter("a")
//            );
//
//            // Search
            FileSearch fileSearch = new FileSearch();
//            List<FileSystemNode> results = fileSearch.find(root, filters);
//
            Filter javaAndLarge =
                    new AndFilter(List.of(
                            new ExtensionsFilter(".java"),
                            new SizeFilter(150, SizeFilter.Operator.GT)
                    ));

            Filter logFiles =
                    new NameContainsFilter("log");

            Filter finalFilter =
                    new OrFilter(List.of(javaAndLarge, logFiles));

            List<FileSystemNode> results =
                    fileSearch.find(root, finalFilter);
// Output
            for (FileSystemNode file : results) {
                System.out.println(file.getName());
            }
        }
    }