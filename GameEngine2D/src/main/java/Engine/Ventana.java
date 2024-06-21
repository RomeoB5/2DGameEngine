package Engine;

import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Ventana {
    private int ancho, alto;
    private String titulo;
    // The window handle
    private long glfWventana;
    public float r,g,b,a;

    private static Ventana ventana = null;

    private static Scene currentScene;

    private Ventana() {
        this.ancho = 1920;
        this.alto = 1080;
        this.titulo = "Juego";
        r = 0;
        b = 1;
        g = 0;
        a = 0;
    }

    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false: "Escena desconocida '" + newScene + "'";
                break;
        }
    }

    public static Ventana get(){
        if (Ventana.ventana == null){
            Ventana.ventana = new Ventana();
        }
        return Ventana.ventana;
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfWventana);
        glfwDestroyWindow(glfWventana);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfWventana = glfwCreateWindow(this.ancho, this.alto, this.titulo, NULL, NULL);
        if ( glfWventana == NULL ) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(glfWventana, KeyListener::keyCallback);
        //glfwSetMouseButtonCallback(glfWventana, MouseListener::mouseButtonCallback);
        //glfwSetScrollCallback(glfWventana, MouseListener::mouseScrollCallback);
        //glfwSetCursorPosCallback(glfWventana, MouseListener::mousePosCallback); // No funciona

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfWventana);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfWventana);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Ventana.changeScene(0);
    }

    private void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfWventana)){
            if(KeyListener.isKeyPressed((GLFW_KEY_W))) {
                currentScene.camera.position.y -= dt * 40.0f;
            }
            else if(KeyListener.isKeyPressed((GLFW_KEY_S))){
                currentScene.camera.position.y += dt * 40.0f;
            }
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // Set the clear color
            glClearColor(r, b, g, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0) {
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfWventana); // swap the color buffers

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}

