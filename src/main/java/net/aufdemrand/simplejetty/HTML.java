package net.aufdemrand.simplejetty;

import java.util.*;


public class HTML {

    //
    // Constructors

    // Use for general HTML output
    public HTML() { /* ... */ }

    public boolean inside_table = false;

    //
    // Innards
    private List<Container> containers = new ArrayList<Container>();
    private StringBuilder html = new StringBuilder();

    //
    // Public methods

    // Open a new Container
    public HTML open(Container container) {
        html.append(container.open());
        containers.add(container);
        return this;
    }

    // Close a container
    public void close() { close(1); }

    public void close(int containers) {
        while (containers > 0) {
            html.append(this.containers.get(this.containers.size() - 1).close());
            this.containers.remove(this.containers.size() - 1);
            containers--;
        }
    }

    @Override
    public String toString() {
        return html.toString();
    }


    //
    // HTML Container

    public static class Container {

        private String surround;
        private String content;
        private String id;
        private Map<String, String> attribs = new HashMap<String, String>();
        private Map<String, String> focusables = new TreeMap<String, String>();

        public Container (String constructor) {
            String[] args = constructor.split("\\.", 2);
            surround = args[0];
            if (args.length > 1)
                attribs.put("class", args[1]);
        }

        public Container setContent(String content) {
            this.content = content + '\n';
            return this;
        }

        public Container setId(String id) {
            this.id = id;
            return this;
        }

        public Container addAttr(String attrib, String value) {
            this.attribs.put(attrib, value);
            return this;
        }

        private String open() {
            StringBuilder sb = new StringBuilder();

            // Open tag, insert id
            sb.append("<" + surround
                    + (id != null ? " id='" + id + "' " : ""));

            // Iterate through attribs
            for (Map.Entry<String, String> attrib : attribs.entrySet())
                sb.append(' ' + attrib.getKey() + "='" + attrib.getValue() + "'");

            sb.append(">" + '\n');

            // Add content
            if (content != null)
                sb.append(content);

            return sb.toString();
        }


        private String close() {
            return "</" + surround + ">\n";
        }

    }



}
