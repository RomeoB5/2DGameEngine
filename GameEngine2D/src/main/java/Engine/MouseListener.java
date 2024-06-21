package Engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
        this.isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] ||get().mouseButtonPressed[2];
    }

    public static MouseListener get(){
        if(instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long ventana, double xpos, double ypos){
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
    }

    public static void mouseButtonCallback(long ventana, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            // Cuando se presiona un botón del mouse
            if (button < get().mouseButtonPressed.length) {
                // Marcar el botón como presionado
                get().mouseButtonPressed[button] = true;

                // Aquí podrías realizar acciones adicionales al presionar el botón
            }
        } else if (action == GLFW_RELEASE) {
            // Cuando se libera un botón del mouse
            if (button < get().mouseButtonPressed.length) {
                // Marcar el botón como liberado
                get().mouseButtonPressed[button] = false;

                // Reiniciar cualquier estado relacionado con el arrastre, si es necesario
                get().isDragging = false;

                // Aquí podrías realizar acciones adicionales al liberar el botón
            }
        }
    }


    public static void mouseScrollCallback(long ventana, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX(){
        return (float)get().xPos;
    }

    public static float getY(){
        return (float)get().yPos;
    }

    public static float getDx(){
        return (float)(get().lastX - get().xPos);
    }

    public static float getDy(){
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX(){
        return (float)get().scrollX;
    }

    public static float getScrollY(){
        return (float)get().scrollY;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        }
        else{
            return false;
        }
    }
}
