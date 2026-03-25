# ANDIE by Great People

## Authors:
* Maleena Taia
* Loek Van Broekhoven
* Ritvik Sharma
* Robert Hannaford

## How to Use:

Begin by opening an image using the open button under the file drop-down menu. You may need to resize the image if the image is large. Additional zoom tools are available under the _view_ tab. You may then choose from the range of filters and functions under the _edit_, _filter_, and _colour_ options. To save an image you have finished making changes to, click _save_ under the _file_ tab, or to save a new copy, click _save as_. If you wish to save it under a different file type, instead click _export_ and choose an option under the _files of type_ field under the _file name_ field.

## New operations:

### File

* Export - 

### Edit

* Resize - The image will be scaled up or down depending on the factor provided (a percentage). If it is less than 100% the image will be scaled down, else the image will be scaled up accordingly.
* Rotate 90 Clockwise - Rotate a image by 90 degrees clockwise.
* Rotate 90 Anti-Clockwise - rotate a image by 90 degrees anti-clockwise.
* Rotate 180 degrees - rotate a image by 180 degrees (mirror image).
* Horizontal flip - Flip image horizontally in place.
* Vertical flip - Flip image vertically in place.

### View

Unchanged as of 20/03

### Filter

* Sharpen - Applys a sharpen filter to the working image using a kernel and convolution. No user input is required.
* Gaussian - Applys a Gaussian filter to the working image using a given radius to apply values to a kernel and then is applied via convolution. This has a maximum radius of 10 and will default to 1 if limits are exceeded or subceeded, though using the built in menu will confine itself to the limits.
* Median - Applys a median filter to the working image by averaging the colour channel (inlcuding alpha channel) of each surrounding pixel and then making the new pixel the median of the surrounding pixels. This operation is applied to a new image so it does not reuse already processed pixels. This has a maximum radius of 10 and will default to 1 if limits are exceeded or subceeded, though using the built in menu will confine itself to the limits. (Note: this operation is O(n^4) so take care if using radius > 10)

### Colour

* Threshold - Converts a colour image to black and white based on a specified intensity threshold. Pixels with average brightness above the threshold become white, and those below become black. The original alpha (transparency) of each pixel is preserved. The threshold value is provided by the user, between 0 and 255.
* Colour Channel Swapping - Reorders the red, green, and blue channels of the working image according to a user-specified permutation (e.g., RGB, GBR, BRG). The user inputs the desired channel order via a dialog box. Alpha (transparency) values are preserved. Invalid input will leave the image unchanged.
* Image Inversion - Inverts all colours of the working image by subtracting each RGB channel from 255. For example, a pixel with (R=100, G=150, B=200) becomes (R=155, G=105, B=55). The alpha (transparency) channel is preserved. No user input is required.

## Testing

## Known bugs

### Edge detection on certain blur filters

Edge detection hasn't been implemented for blur filters using Kernels and convolution.

## Refactoring

### Internationalisation

ANDIE's provided ui has been internationalised and an I18N manager has been added.