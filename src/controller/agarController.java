package controller;

import java.awt.*;
import java.awt.event.*;
import view.*;
import model.*;

import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;

import javax.swing.*;

class Agar {
    public static void main(String[] args) {
        ControllerOptions opt = new ControllerOptions();

        for (String arg : args) {
            opt.parse(arg);
        }

        final AgarController controller = new AgarController(opt);

        controller.loop();
    }
}

class ControllerOptions {
    private int framerate = 30;
    private String player = "Bengt";
    private char playerEast = 'a';
    private char playerWest = 'd';
    private char playerNorth = 'w';
    private char playerSouth = 's';

    public ControllerOptions parse(String opt) {
        try {
            String[] parts = opt.split("=");
            switch (parts[0]) {
                case "--help":
                    this.printUsage();
                    System.exit(0);
                case "--framerate":
                    this.framerate = new Integer(parts[1]);
                    break;
                case "--player":
                    this.player = parts[1];
                    break;
                default:
                    System.err.println("Unrecognized option: \""+opt+"\"");
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Couldn't parse option \""+opt+"\": " + e);
            System.exit(1);
        }
        return this;
    }

    private void printUsage() {
        System.out.println("agarAI, an implementation of agar with bots");
        System.out.println("--help                   : show this help");
        System.out.println("--framerate=<int>        : set the target framerate (and game speed)");
        System.out.println("--player=<name>          : set the player's name");
        System.out.println();
    }

    public int getFramerate() {
        return framerate;
    }
    public String getPlayer() {
        return player;
    }
    public char getPlayerEast() {
        return playerEast;
    }
    public char getPlayerWest() {
        return playerWest;
    }
    public char getPlayerNorth() {
        return playerNorth;
    }
    public char getPlayerSouth() {
        return playerSouth;
    }
}

class AgarController implements KeyListener {
    private final ControllerOptions options;
    private final Map<Character, Input> inputMap;
    private final Set<Input> input = new HashSet<Input>();
    private final AgarView view;
    private final AgarModel model;

    public AgarController(final ControllerOptions opt) {
        this.options = opt;
        this.model = new AgarModel(opt.getFramerate());
        this.view = new AgarView(model);

        this.inputMap = Collections.unmodifiableMap(new HashMap<Character,Input>() {{
            put(options.getPlayerWest(), new Input(Input.Dir.WEST));
            put(options.getPlayerEast(), new Input(Input.Dir.EAST));
            put(options.getPlayerNorth(), new Input(Input.Dir.NORTH));
            put(options.getPlayerSouth(), new Input(Input.Dir.SOUTH));
        }});
    }

    public void keyPressed(KeyEvent e) { }

    public void keyTyped(KeyEvent e) {
        if (inputMap.containsKey(e.getKeyChar())) {
            input.add(inputMap.get(e.getKeyChar()));
        }
    }

    public void keyReleased(KeyEvent e) {
        if (inputMap.containsKey(e.getKeyChar())) {
            input.remove(inputMap.get(e.getKeyChar()));
        }
    }

    public void loop() {
        final KeyListener This = this;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.getFrame().addKeyListener(This);
                view.show();
            }
        });

        long target_delta = 1000/options.getFramerate();
        long delta = 1000/options.getFramerate();
        for (;;) {
            try {
                long last_compute = System.currentTimeMillis();
                model.compute(input, delta);
                view.update();

                delta = System.currentTimeMillis() - last_compute;

                if (delta < target_delta) {
                    Thread.currentThread().sleep(target_delta-delta);
                }
                delta = System.currentTimeMillis() - last_compute;


            } catch (Exception ignore) {}
        }
    }
}