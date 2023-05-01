package estu.ceng.edu;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {

        Options options = new Options();
        CmdLineParser parser = new CmdLineParser(options);

        String fileName = options.fileName;
        List<Node> nodes = new ArrayList<>();

        boolean success = true;
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            success = false;
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
        if (!success) {
            System.exit(1);
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }

                String[] tokens = line.split("->");

                if (tokens.length == 1) {
                    Node node = new Node(tokens[0]);
                    nodes.add(node);
                } else {
                    String[] dependencies = tokens[0].split(",");
                    Node node = new Node(tokens[1]);

                    nodeAddition(nodes, dependencies, node);
                }
            }

            for (Node node : nodes) {
                node.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void dependencyAdditionToNodes(List<Node> nodes, String[] dependencies, Node node) {
        for(String dependency : dependencies) {
            Node dependencyNode = null;

            for(Node n : nodes) {
                if(n.getName().equals(dependency)) {
                    dependencyNode = n;
                    break;
                }
            }

            if(dependencyNode == null) {
                dependencyNode = new Node(dependency);
                nodes.add(dependencyNode);
            }
            node.addingDependency(dependencyNode);
        }
    }

    private static void nodeAddition(List<Node> nodes, String[] dependencies, Node node) {
        int index = nodes.indexOf(node);

        if(index == -1) {
            dependencyAdditionToNodes(nodes, dependencies, node);
            nodes.add(node);
        } else {
            Node existingNode = nodes.get(index);
            dependencyAdditionToNodes(nodes, dependencies, existingNode);
        }
    }
}