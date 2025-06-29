package com.example.wheelchairapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLES20;
import javax.microedition.khronos.opengles.GL10;

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // OpenGL ES 2.0을 사용하도록 설정
        setEGLContextClientVersion(2);

        // Renderer 설정
        renderer = new MyGLRenderer();
        setRenderer(renderer);
    }

    private static class MyGLRenderer implements GLSurfaceView.Renderer {
        // 여기에서 OpenGL 설정 및 초기화를 수행
        @Override
        public void onSurfaceCreated(GL10 gl10, javax.microedition.khronos.egl.EGLConfig eglConfig) {
            // OpenGL 초기화 코드
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            // 렌더링 코드
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            // 화면 크기 변경 시 호출되는 코드
            GLES20.glViewport(0, 0, width, height);
        }
    }
}
