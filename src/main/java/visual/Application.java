package visual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import controller.Controller;
import controller.IController;
import javafx.animation.AnimationTimer;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.stage.Stage;
import model.Point;
import model.dto.MapInfoDTO;
import model.dto.MineSnakeDTO;

public class Application extends javafx.application.Application {
    private static IController controller;
    public static void main(String[] args) {
        controller = new Controller(true);
        launch(args);
    }

    private MapInfoDTO getMapInfo() {
        return controller.getMapInfo(null);
    }

    private final static int CAMERA_OFFSET = 20;

    private double cameraX = 0;
    private double cameraY = 0;
    private double cameraZ = 0;
    private double cameraRotateZ = 0;
    private double cameraRotateX = 0;
    private double cameraRotateY = 0;

    private void setCameraPosition(MapInfoDTO mapInfoDTO) {
        MineSnakeDTO alive = mapInfoDTO.snakes.stream().filter(s -> s.status.equals("alive"))
                .findAny().orElse(null);
        if (alive == null) {
            System.err.println("WARNING ALL SNAKES ARE DEAD");
            cameraX = 0;
            cameraY = 0;
            cameraZ = 0;
            return;
        }
        Point head = alive.geometry.get(0);
//        cameraX = 0;
//        cameraY = 0;
//        cameraZ = 0;
        cameraX = head.coors.get(0);
        cameraY = head.coors.get(1);
        cameraZ = head.coors.get(2);
    }

    private final static PhongMaterial ENEMY_COLOR = new PhongMaterial(Color.RED);
    private final static PhongMaterial WE_COLOR = new PhongMaterial(Color.BLUE);
    private final static PhongMaterial ORANGE_COLOR = new PhongMaterial(Color.ORANGE);
    private final static PhongMaterial WALL_COLOR = new PhongMaterial(Color.GREEN);
    private final static PhongMaterial BAD_COLOR = new PhongMaterial(Color.MAGENTA);
    private final static PhongMaterial GOOD_COLOR = new PhongMaterial(Color.BLACK);
    private final static int BOX_SIZE = 1;

    private List<Box> getBoxes(MapInfoDTO mapInfoDTO) {
        List<Box> res = new ArrayList<>();
        mapInfoDTO.snakes.forEach(s -> {
            s.geometry.forEach(p -> {
                Box box = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
                box.setCullFace(CullFace.NONE);
                box.setTranslateX(p.coors.get(0));
                box.setTranslateY(p.coors.get(1));
                box.setTranslateZ(p.coors.get(2));
                box.setMaterial(WE_COLOR);
                res.add(box);
            });
        });

        mapInfoDTO.enemies.forEach(s -> {
            s.geometry.forEach(p -> {
                Box box = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
                box.setCullFace(CullFace.NONE);
                box.setTranslateX(p.coors.get(0));
                box.setTranslateY(p.coors.get(1));
                box.setTranslateZ(p.coors.get(2));
                box.setMaterial(ENEMY_COLOR);
                res.add(box);
            });
        });

        mapInfoDTO.fences.forEach(p -> {
            Box box = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
            box.setCullFace(CullFace.NONE);
            box.setTranslateX(p.coors.get(0));
            box.setTranslateY(p.coors.get(1));
            box.setTranslateZ(p.coors.get(2));
            box.setMaterial(WALL_COLOR);
            res.add(box);
        });

        Set<Point> has = new HashSet<>();
        mapInfoDTO.specialFood.golden.forEach(f -> {
            Box box = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
            box.setCullFace(CullFace.NONE);
            box.setTranslateX(f.coors.get(0));
            box.setTranslateY(f.coors.get(1));
            box.setTranslateZ(f.coors.get(2));
            box.setMaterial(GOOD_COLOR);
            res.add(box);
            has.add(f);
        });
        mapInfoDTO.specialFood.suspicious.forEach(f -> {
            Box box = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
            box.setCullFace(CullFace.NONE);
            box.setTranslateX(f.coors.get(0));
            box.setTranslateY(f.coors.get(1));
            box.setTranslateZ(f.coors.get(2));
            box.setMaterial(BAD_COLOR);
            res.add(box);
            has.add(f);
        });
        mapInfoDTO.food.forEach(f -> {
            var p = f.c;
            if (has.contains(p)) {
                return;
            }
            Box box = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
            box.setCullFace(CullFace.NONE);
            box.setTranslateX(p.coors.get(0));
            box.setTranslateY(p.coors.get(1));
            box.setTranslateZ(p.coors.get(2));
            box.setMaterial(ORANGE_COLOR);
            res.add(box);
        });
        return res;
    }

    private void matrixRotateNode(Node n, double alf, double bet, double gam) {
        double A11 = Math.cos(alf) * Math.cos(gam);
        double A12 = Math.cos(bet) * Math.sin(alf) + Math.cos(alf) * Math.sin(bet) * Math.sin(gam);
        double A13 = Math.sin(alf) * Math.sin(bet) - Math.cos(alf) * Math.cos(bet) * Math.sin(gam);
        double A21 = -Math.cos(gam) * Math.sin(alf);
        double A22 = Math.cos(alf) * Math.cos(bet) - Math.sin(alf) * Math.sin(bet) * Math.sin(gam);
        double A23 = Math.cos(alf) * Math.sin(bet) + Math.cos(bet) * Math.sin(alf) * Math.sin(gam);
        double A31 = Math.sin(gam);
        double A32 = -Math.cos(gam) * Math.sin(bet);
        double A33 = Math.cos(bet) * Math.cos(gam);

        double d = Math.acos((A11 + A22 + A33 - 1d) / 2d);
        if (d != 0d) {
            double den = 2d * Math.sin(d);
            Point3D p = new Point3D((A32 - A23) / den, (A13 - A31) / den, (A21 - A12) / den);
            n.setRotationAxis(p);
            n.setRotate(Math.toDegrees(d));
        }
    }

    private double getRad(double ang) {
        return (ang * Math.PI) / 180.0;
    }

    @Override
    public void start(Stage stage) throws Exception {
        boolean is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
        if (!is3DSupported) {
            System.out.println("Sorry, 3D is not supported in JavaFX on this platform.");
            return;
        }

        MapInfoDTO initMapInfo = getMapInfo();

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10000);
        camera.setNearClip(0.1);
        setCameraPosition(initMapInfo);
//        System.out.println(cameraPos.coors);
//        camera.setTranslateX(cameraPos.coors.get(0));
//        camera.setTranslateY(cameraPos.coors.get(1));
//        camera.setTranslateZ(cameraPos.coors.get(2));

        var mainGroup = new Group();
        var boxes = getBoxes(initMapInfo);
        mainGroup.getChildren().addAll(boxes);

        Scene scene = new Scene(mainGroup, 1500, 800, true);
        scene.setCamera(camera);

        // Handle camera movement with key events
        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            if (key == KeyCode.W) {
                cameraZ -= 1;
            } else if (key == KeyCode.S) {
                cameraZ += 1;
            } else if (key == KeyCode.A) {
                cameraX -= 1;
            } else if (key == KeyCode.D) {
                cameraX += 1;
            } else if (key == KeyCode.Q) {
                cameraY -= 1;
            } else if (key == KeyCode.E) {
                cameraY += 1;
            } else if (key == KeyCode.LEFT) {
                cameraRotateZ -= 10;
                matrixRotateNode(camera, getRad(cameraRotateX), getRad(cameraRotateY), getRad(cameraRotateZ));
            } else if (key == KeyCode.RIGHT) {
                cameraRotateZ += 10;
                matrixRotateNode(camera, getRad(cameraRotateX), getRad(cameraRotateY), getRad(cameraRotateZ));
            } else if (key == KeyCode.UP) {
                cameraRotateX += 10;
                matrixRotateNode(camera, getRad(cameraRotateX), getRad(cameraRotateY), getRad(cameraRotateZ));
            } else if (key == KeyCode.DOWN) {
                cameraRotateX -= 10;
                matrixRotateNode(camera, getRad(cameraRotateX), getRad(cameraRotateY), getRad(cameraRotateZ));
            } else if (key == KeyCode.OPEN_BRACKET) {
                cameraRotateY -= 10;
                matrixRotateNode(camera, getRad(cameraRotateX), getRad(cameraRotateY), getRad(cameraRotateZ));
            } else if (key == KeyCode.CLOSE_BRACKET) {
                cameraRotateY += 10;
                matrixRotateNode(camera, getRad(cameraRotateX), getRad(cameraRotateY), getRad(cameraRotateZ));
            }
//            System.out.println(camera.getRotationAxis());
//            System.out.println(cameraX);
//            System.out.println(cameraY);
//            System.out.println(cameraZ);
//            System.out.println("-------------------");
        });
        stage.setScene(scene);
        stage.setTitle("3D Example");

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                var newMapInfo = getMapInfo();
                Platform.runLater(() -> {
                    mainGroup.getChildren().clear();
                    mainGroup.getChildren().addAll(getBoxes(newMapInfo));
                });
            }
        });
        thread.start();

        stage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                camera.setTranslateX(cameraX);
                camera.setTranslateY(cameraY);
                camera.setTranslateZ(cameraZ);
            }
        };
        animationTimer.start();

    }
}
