package cn.co.willow.android.ultimate.gpuimage.sample.filter;

import android.content.Context;
import android.opengl.GLES20;

import cn.co.willow.android.ultimate.gpuimage.core_render_filter.GPUImageFilter;
import cn.co.willow.android.ultimate.gpuimage.sample.R;

public class MagicBrooklynFilter extends GPUImageFilter {
	private int[] inputTextureHandles = {-1,-1,-1};
	private int[] inputTextureUniformLocations = {-1,-1,-1};
    private int mGLStrengthLocation;
    private float strength = 2.0f;
    private final Context context;

	public MagicBrooklynFilter(Context context){
		super(NO_FILTER_VERTEX_SHADER, LitOpenGlUtils.readShaderFromRawResource(context, R.raw.brooklyn));
		this.context = context;
	}
	
	public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(inputTextureHandles.length, inputTextureHandles, 0);
        for(int i = 0; i < inputTextureHandles.length; i++)
        	inputTextureHandles[i] = -1;
    }
	
	protected void onDrawArraysAfter(){
		for(int i = 0; i < inputTextureHandles.length
				&& inputTextureHandles[i] != LitOpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i+3));
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		}
	}
	  
	protected void onDrawArraysPre(){
		for(int i = 0; i < inputTextureHandles.length 
				&& inputTextureHandles[i] != LitOpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i+3) );
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i]);
			GLES20.glUniform1i(inputTextureUniformLocations[i], (i+3));
		}
	}
	
	public void onInit(){
		super.onInit();
		for(int i=0; i < inputTextureUniformLocations.length; i++)
			inputTextureUniformLocations[i] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture"+(2+i));
		mGLStrengthLocation = GLES20.glGetUniformLocation(mGLProgId,
				"strength");
	}
	
	public void onInitialized(){
		super.onInitialized();
		setFloat(mGLStrengthLocation, strength);
	    runOnDraw(new Runnable(){
		    public void run(){
		    	inputTextureHandles[0] = LitOpenGlUtils.loadTexture(context, "filter/brooklynCurves1.png");
				inputTextureHandles[1] = LitOpenGlUtils.loadTexture(context, "filter/filter_map_first.png");
				inputTextureHandles[2] = LitOpenGlUtils.loadTexture(context, "filter/brooklynCurves2.png");
		    }
	    });
	}

}
