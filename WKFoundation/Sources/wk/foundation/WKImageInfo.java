//
//  WKImageInfo.java
//  cheetah
//
//  Created by Kieran Kelleher on 5/5/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import java.io.InputStream;

/** A wrapper class for ImageInfo in case I want to switch
    my implementation to some other tool like sips in the future. */
public class WKImageInfo {
    protected ImageInfo _imageInfo;
    protected InputStream _inputStream;
    
    public WKImageInfo( InputStream inputStream ) {
        _inputStream = inputStream;
    }
    
    /** Lazily creates the ImageInfo */
    protected ImageInfo imageInfo() {
        if ( _imageInfo == null ) {
            _imageInfo = new ImageInfo();
            _imageInfo.setInput( _inputStream );
            _imageInfo.check();
        }
        return _imageInfo;
    }
    
    public void setInput( InputStream inputStream ) {
        _inputStream = inputStream;
        _imageInfo.setInput( _inputStream );
        _imageInfo.check();
    }
    
    /** @return the height of the image in pixels */
    public int pixelHeight() {
        return imageInfo().getHeight();
    }
    
    /** @return the width of the image in pixels */
    public int pixelWidth() {
        return imageInfo().getWidth();
    }
    
    
}
