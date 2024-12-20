package visual;

import java.util.ArrayList;
import java.util.List;

import controller.ControllerStub;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Point;
import model.dto.MapInfoDTO;
import model.dto.MineSnakeDTO;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    private MapInfoDTO getMapInfo() {
        // TODO change
        return new ControllerStub().getMapInfo(null);
    }

    private final static int CAMERA_OFFSET = 20;

    private double cameraX = 0;
    private double cameraY = 0;
    private double cameraZ = 0;

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
        cameraX = 0;
        cameraY = 0;
        cameraZ = 0;
//        cameraX = head.coors.get(0) - CAMERA_OFFSET;
//        cameraY = head.coors.get(1);
//        cameraZ = head.coors.get(2);
    }

    private final static PhongMaterial ENEMY_COLOR = new PhongMaterial(Color.RED);
    private final static PhongMaterial WE_COLOR = new PhongMaterial(Color.BLUE);
    private final static PhongMaterial ORANGE_COLOR = new PhongMaterial(Color.ORANGE);
    private final static PhongMaterial WALL_COLOR = new PhongMaterial(Color.GREEN);
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

        mapInfoDTO.food.forEach(f -> {
            var p = f.c;
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

    @Override
    public void start(Stage stage) throws Exception {
        boolean is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
        if (!is3DSupported) {
            System.out.println("Sorry, 3D is not supported in JavaFX on this platform.");
            return;
        }

        MapInfoDTO mapInfoDTO = getMapInfo();

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10000);
        camera.setNearClip(0.1);
        setCameraPosition(mapInfoDTO);
//        System.out.println(cameraPos.coors);
//        camera.setTranslateX(cameraPos.coors.get(0));
//        camera.setTranslateY(cameraPos.coors.get(1));
//        camera.setTranslateZ(cameraPos.coors.get(2));

        var mainGroup = new Group();
        var boxes = getBoxes(mapInfoDTO);
        mainGroup.getChildren().addAll(boxes);

        Scene scene = new Scene(mainGroup, 1500, 800, true);
        scene.setCamera(camera);

        boolean[] clockwiseRotate = new boolean[]{false};
        RotateTransition clockwiseRotateTransition = new RotateTransition(Duration.seconds(5), camera);
        clockwiseRotateTransition.setAxis(Rotate.Y_AXIS);
        clockwiseRotateTransition.setFromAngle(0);
        clockwiseRotateTransition.setToAngle(360);
        clockwiseRotateTransition.setInterpolator(Interpolator.LINEAR);
        clockwiseRotateTransition.setCycleCount(RotateTransition.INDEFINITE);

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
            } else if (key == KeyCode.R) {
                clockwiseRotate[0] ^= true;
                clockwiseRotateTransition.setToAngle(Math.abs(clockwiseRotateTransition.getToAngle()));
                if (clockwiseRotate[0]) {
                    clockwiseRotateTransition.play();
                } else {
                    clockwiseRotateTransition.pause();
                }
            }
            System.out.println(cameraX);
            System.out.println(cameraY);
            System.out.println(cameraZ);
            System.out.println("-------------------");
        });
        stage.setScene(scene);
        stage.setTitle("3D Example");

//        Thread thread = new Thread(() -> {
//            for (int i = 0; i < 1000; ++i) {
//                Platform.runLater(() -> {
//                    camera.setTranslateX(camera.getTranslateX() + 1);
//                });
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();

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
