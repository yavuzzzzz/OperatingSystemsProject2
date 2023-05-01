package estu.ceng.edu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node extends Thread {

    private final String name;
    private final List<Node> dependencies;

    public Node(String name) {
        this.name = name;
        this.dependencies = new ArrayList<>();
    }

    public void addingDependency(Node dependency) {
        dependencies.add(dependency);
    }

    public void run() {
        if (!dependencies.isEmpty()){
            System.out.println("Node" + name + " is waiting for " + dependencies.stream().map(node -> node.name).toList());
        }

        for(Node dep: dependencies){
            if(dep.isAlive()){
                boolean interrupted = false;
                while (true) {
                    try {
                        dep.join();
                        break;
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                }
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        System.out.println("Node" + name + " is being started");
        perform();
        System.out.println("Node" + name + " is completed.");
    }

    @Override
    public boolean equals(Object o){
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    public void perform() {
        int time = (int)(Math.random() * 2000) + 1;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
