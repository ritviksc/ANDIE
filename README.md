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

* Resize -
* Rotate 90 Clockwise -
* Rotate 90 Anti-Clockwise -
* Rotate 180 degrees - 
* Horizontal flip -
* Vertical flip -

### View

Unchanged as of 20/03

### Filter

* Sharpen - Applys a sharpen filter to the working image using a kernel and convolution. No user input is required.
* Gaussian - Applys a Gaussian filter to the working image using a given radius to apply values to a kernel and then is applied via convolution. This has a maximum radius of 10 and will default to 1 if limits are exceeded or subceeded, though using the built in menu will confine itself to the limits.
* Median - Applys a median filter to the working image by averaging the colour channel (inlcuding alpha channel) of each surrounding pixel and then making the new pixel the median of the surrounding pixels. This operation is applied to a new image so it does not reuse already processed pixels. This has a maximum radius of 10 and will default to 1 if limits are exceeded or subceeded, though using the built in menu will confine itself to the limits. (Note: this operation is O(n^4) so take care if using radius > 10)

### Colour

* Threshold -
* Colour Channel Swapping -

## Testing

### File

* Export - 

### Edit

* Resize -
* Rotate 90 Clockwise -
* Rotate 90 Anti-Clockwise -
* Rotate 180 degrees - 
* Horizontal flip -
* Vertical flip -

### Filter

* Sharpen - 
* Gaussian - 
* Median - 

### Colour

* Threshold -
* Colour Channel Swapping -

## Known bugs

### JPG transparency export error

When exporting a JPG with transparency in the current working image, no error message is shown and the file is not exported.

### Edge detection on certain blur filters

Edge detection hasn't been implemented for blur filters using Kernels and convolution.

## Refactoring

### Internationalisation

ANDIE's provided ui has been internationalised.