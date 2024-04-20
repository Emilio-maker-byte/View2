package com.example.view2;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.robot.Robot;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.net.MalformedURLException;
import java.util.ResourceBundle;
import java.util.Timer;





public class Main extends Application {


    Box myBox = new Box(5,5,5);
    Box floor = new Box(500,1,500);
    int FPS = 30;
    boolean running;
    Robot rb = new Robot();
    SubScene trueScene;

    double mouseDeltaX = 0;
    double mouseDeltaY = 0;

    double mouseX;
    double mouseY;
    double ZMovement = 0;
    double XMovement = 0;

    int zStatus = 0;
    int xStatus = 0;

    int jump = 0;

    File f = new File("C:/Users/emili/OneDrive/Desktop/RotatingCube.mp4");
    Media m = new Media(f.toURI().toURL().toString());
    MediaPlayer player = new MediaPlayer(m);
    MediaView view = new MediaView(player);

    //camera rotation
//    double cameraXRotation = 0;
//    double cameraYRotation = 0;

    Rotate cameraXRotation = new Rotate(0, Rotate.X_AXIS);
    Rotate cameraYRotation = new Rotate(0, Rotate.Y_AXIS);

//    Translate cameraZMovement = new Translate();
//    Translate cameraSideMovement = new Translate();

    Translate cameraMovement = new Translate();

    Rectangle shape = new Rectangle(50,50);
    Text text = new Text();

    Group shapeGroup = new Group();

    public Main() throws MalformedURLException {
    }


    @Override
    public void start(Stage stage) throws Exception {

        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);

        view.setPreserveRatio(true);
//        view.setFitHeight(50);
        view.setFitWidth(250);

//        view.setX(30);
        view.setY(0);

        //Create a 3D Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        
        mouseX = rb.getMouseX();
        mouseY = rb.getMouseY();

        Timeline timeline = new Timeline();

        camera.getTransforms().add(cameraYRotation);
        camera.getTransforms().add(cameraMovement);
        camera.getTransforms().add(cameraXRotation);

        timeline.getKeyFrames().add(new KeyFrame(
                new Duration(10),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        //mouse left right rotation
                        mouseDeltaX = -1*(mouseX - rb.getMouseX());
                        cameraYRotation.setAngle((cameraYRotation.getAngle() + mouseDeltaX/2)%360);
                        mouseX = rb.getMouseX();

                        //keeps camera rotation within 1-360
                        if(cameraYRotation.getAngle() < 1 ){
                            cameraYRotation.setAngle(360 + cameraYRotation.getAngle());
                        }

                        //changes camera's pivot based on camera position
                        cameraYRotation.setPivotX(cameraMovement.getX());
                        cameraYRotation.setPivotZ(cameraMovement.getZ());


                        //Direction overlayww
//                        System.out.println(cameraYRotation.getAngle());
                        text.setText("Angle: " + (int) cameraYRotation.getAngle()+"");

                        // Z = Forward/Backward
                        // X = Left/Right

                        // 0 - not pressed
                        // 1 - positive
                        // 2 - negative

                        if(zStatus != 0) {
                            if(zStatus == 1) {
                                ZMovement = (Math.abs((cameraYRotation.getAngle()%180)-90)/90);
                                XMovement = 1 - ZMovement;
                                switchAxis();
                            }

                            if(zStatus == 2) {
                                ZMovement = -(Math.abs((cameraYRotation.getAngle()%180)-90)/90);
                                XMovement = -1 - ZMovement;
                                switchAxis();
                            }

                            //repositions camera's position
                            cameraMovement.setZ(cameraMovement.getZ() + ZMovement);
                            cameraMovement.setX(cameraMovement.getX() + XMovement);

//                            zStatus = 0;
                        }

                        if(xStatus != 0) {
                            if(xStatus == 1) {
                                XMovement = (Math.abs((cameraYRotation.getAngle()%180)-90)/90);
                                ZMovement = -1 + XMovement;
                                switchSideAxis();
                            }

                            if(xStatus == 2) {
                                XMovement = -(Math.abs(((cameraYRotation.getAngle())%180)-90)/90);
                                ZMovement = 1 + XMovement;
                                switchSideAxis();
                            }

//                            System.out.println((Math.abs(ZMovement) + Math.abs(XMovement)));

                            //repositions camera's position
                            cameraMovement.setZ(cameraMovement.getZ() + ZMovement);
                            cameraMovement.setX(cameraMovement.getX() + XMovement);

                        }

                        if(jump == 2 /*&& cameraMovement.getY() > -200*/) {
                            cameraMovement.setY(cameraMovement.getY() - jump);
                        }
                        if(jump == 0 && cameraMovement.getY() < 0){
                            cameraMovement.setY(cameraMovement.getY() + 0.5);
                        }
//                        System.out.println(cameraMovement.getY());
//                        System.out.println(jump);


//                        System.out.println(cameraYRotation.getAngle());

//                        //repositions camera's position
//                        cameraMovement.setZ(cameraMovement.getZ() + ZMovement);
//                        cameraMovement.setX(cameraMovement.getX() + XMovement);

//                        System.out.println("X: " + XMovement);
//                        System.out.println("Z: " + ZMovement);
//                        System.out.println((Math.abs(ZMovement) + Math.abs(XMovement)));

                        //mouse up down rotation
                        if(cameraXRotation.getAngle() < 75 && cameraXRotation.getAngle() > -75) {
                            mouseDeltaY = (mouseY - rb.getMouseY());
                            cameraXRotation.setAngle((cameraXRotation.getAngle() + mouseDeltaY/2)%360);
                            mouseY = rb.getMouseY();
                        }
                        if (cameraXRotation.getAngle() <= -75) {
                            cameraXRotation.setAngle(-74);
                        }
                        if (cameraXRotation.getAngle() >= 75) {
                            cameraXRotation.setAngle(74);
                        }
                    }
                }
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        stage.setResizable(true);

        AmbientLight light = new AmbientLight();
        light.setColor(Color.WHITE);

        //floor
        floor.getTransforms().add(new Translate(0,25,-50));
        PhongMaterial floorMaterial = new PhongMaterial();
        floorMaterial.setDiffuseMap(new Image("https://preview.redd.it/so-thoughts-on-this-top-grass-texture-v0-3019db7xleg91.png?auto=webp&s=9c3e10e00fa047dc4bcc1906650e84ac80632007"));
//        floorMaterial.setBumpMap(new Image("https://docs.unity3d.com/uploads/Main/BumpMapTexturePreview.png"));
        floor.setMaterial(floorMaterial);
        floor.setDrawMode(DrawMode.FILL);

        // Box
        myBox.setMaterial(new PhongMaterial(Color.GREEN));
        myBox.setDrawMode(DrawMode.LINE);


        Circle c = new Circle();
        c.setRadius(5);

        camera.setFarClip(1000.0);
        camera.setFieldOfView(75);

        // Build a Scene Graph
        Group root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(floor);
//        root.getChildren().add(view);

//        root.getChildren().add(myBox);
        root.getChildren().add(light);

//        root.getChildren().add(overlay);

        //SubScene
        trueScene = new SubScene(root, 160*5, 90*5);
        trueScene.setCamera(camera);

        Scene testScene = new Scene(shapeGroup);

        testScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.W) {
                    zStatus = 0;
                }
                if(event.getCode() == KeyCode.A) {
                    xStatus = 0;
                }
                if(event.getCode() == KeyCode.S) {
                    zStatus = 0;
                }
                if(event.getCode() == KeyCode.D) {
                    xStatus = 0;
                }
                if(event.getCode() == KeyCode.SPACE) {
                    jump = 0;
                }
            }
        });

        testScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                // 0 - not pressed
                // 1 - positive
                // 2 - negative

                if(event.getCode() == KeyCode.W) {
                    zStatus = 1;
                }
                if(event.getCode() == KeyCode.S) {
                    zStatus = 2;
                }
                if(event.getCode() == KeyCode.D) {
                    xStatus = 1;
                }
                if(event.getCode() == KeyCode.A) {
                    xStatus = 2;
                }

                if(event.getCode() == KeyCode.SPACE) {
                    jump = 2;
                }

                // press Esc to pause and unpause
                if(event.getCode() == KeyCode.ESCAPE && !timeline.getStatus().toString().equals("PAUSED")) {
                    timeline.pause();
                    System.out.println(timeline.getStatus());
                }else if(event.getCode() == KeyCode.ESCAPE && timeline.getStatus().toString().equals("PAUSED")) {
                    timeline.play();
                }

            }
        });


        shapeGroup.getChildren().add(trueScene);

//        shapeGroup.getChildren().add(shape);

        text.resize(20,20);
        text.setY(20);
        text.setX(20);
        shapeGroup.getChildren().add(text);
        shapeGroup.getChildren().add(view);

        testScene.setCursor(Cursor.NONE);

        stage.setScene(testScene);

//        stage.setScene(trueScene);
        stage.show();

    }
    public void switchAxis() {
        if(cameraYRotation.getAngle() > 90 && cameraYRotation.getAngle() < 270) {
            ZMovement = ZMovement * -1;
        }

        if(cameraYRotation.getAngle() > 180 && cameraYRotation.getAngle() < 360) {
            XMovement = XMovement * -1;

        }
    }

    public void switchSideAxis() {

        if(cameraYRotation.getAngle() > 180 && cameraYRotation.getAngle() < 360) {
            ZMovement = ZMovement * -1;
        }

        if(cameraYRotation.getAngle() > 90 && cameraYRotation.getAngle() < 270) {
            XMovement = XMovement * -1;
        }

    }


    public static void main(String[] args) {
        launch();
    }
}
