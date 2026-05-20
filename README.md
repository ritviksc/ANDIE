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

* Export - prompts the users to enter a file name, and select from the possible formats, a decided on the file placement. if the user cancels the export operation its returns to the program. if the users approves to save then image is export as current into the select file directory, if the user tries to export a file that does not support its specific format type its notifies the user with an error message   

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

* Edge detection - All filters have image edge detection and will use the closest available pixel if the kernel overhangs the edge of the image.
* Mean - Applys a box blur using a kernel of a user inputted radius. 
* Sharpen - Applys a sharpen filter to the working image using a kernel and convolution. No user input is required.
* Gaussian - Applys a Gaussian filter to the working image using a given radius to apply values to a kernel and then is applied via convolution. This has a maximum radius of 10 and will default to 1 if limits are exceeded or subceeded, though using the built in menu will confine itself to the limits.
* Median - Applys a median filter to the working image by averaging the colour channel (inlcuding alpha channel) of each surrounding pixel and then making the new pixel the median of the surrounding pixels. This operation is applied to a new image so it does not reuse already processed pixels. This has a maximum radius of 10 and will default to 1 if limits are exceeded or subceeded, though using the built in menu will confine itself to the limits.
* Emboss - Applys an embossing filter to the working image by applying a user specified directional emboss convolution. A kernel is hardcoded for each (cardinal) direction and passed to the convolution based on user input.
* Sobel - Applys a Sobel filter to the working image in a very similar way to emboss except with 3 different hardcoded Sobel kernels.
* FIR - Applys a FIR filter to the working image as another negative compatible filter. Acts as an 'out of the page' edge dection filter.
* Contrast mask - Enhances the local contrast by creating a blurred inverted greyscale version of the image and blending it back with the original image. User inputs a blur radius and strength of the effect.
* Random scattering - Create a scattered effect by replacing each output pixel with a randomly chosen pixel from within a user-specified radius around the original pixel location
* Convolution filter - A dedicated convolution filter class has been implemented into ANDIE rather than using the convolution from the java library.

### Colour

* GreyScale - Converts coloured image into a greyscale image 
* Threshold - Sets image to greyScale then converts image to black and white based on a specified intensity threshold. Pixels with average brightness above the threshold become white, and those below become black. The original alpha (transparency) of each pixel is preserved. The threshold value is provided by the user, between 0 and 255.
* Colour Channel Swapping - Reorders the red, green, and blue channels of the working image according to a user-specified permutation (e.g., RGB, GBR, BRG). The user inputs the desired channel order via a dialog box. Alpha (transparency) values are preserved. Invalid input will leave the image unchanged.
* Image Inversion - Inverts all colours of the working image by subtracting each RGB channel from 255. For example, a pixel with (R=100, G=150, B=200) becomes (R=155, G=105, B=55). The alpha (transparency) channel is preserved. No user input is required.
* Brightness/ Contrast adjustment - Allown user to adjust the brightness and contrast of the working image. The user provides brightness and contrast values, which are applied to each RGB channel while preserving the alpha transparency channel.
* Saturation - Adjusts the colour intensity of the working image. The user provides a saturation value, where lower values make the image more grey and higher values make the colours more vivid. RGB values are adjusted while preserving alpha transparency channel.

### Save when exiting

* Prompts the user to save, save as, exit without saving or cancel whenever the program might exit
* If the user cancels a save as operation it returns to the program and functions as if the user just clicked the cancel on the original save menu.
* Note - does not work on System.exit()
* Development of this system has revealed a more efficient way to implement errors when trying to apply things when no image has been opened by directly adding to the apply super class. This has currently not been implemented.

### Image Cropping and Drawing

* User can now selected a portion of the loaded image to crop and select as the new image to edit. If they dont want to crop, they can select to draw a few different shapes in the selected area with customisation options.
* Summary:
    * User can crop a rectangle or ellipse out of selected region
    * User can draw a rectangle or ellipse on the selected region (fill or outlined)
    * User can draw line between two selected points
    * User decides color of each object from the color palette

## Testing

Newly developed functions were tested with undo and redo operations, saving, saving as and exporting.
The save and exit was tested when switching language as well as using the exit button and exiting via the x.
Not every combination of filter, edit, or colour manipualtion has been tested but they should all _theoretically_ work.
Trying to apply operations without an image will result in an error occuring. As well as trying to open a new image with an image open already.
Trying to export an image to jpeg that has transparency will result in an error message.
Invalid inputs physically cannot be entered into number boxes so there is not much testing required. 

## Miscellaneuos extra featues

### The bomb

Randomly spawns a bomb that if not clicked on in time will close the program without saving your work.
Note - not internationalised

### Avatar

Adds an avatar to the bottom right corner of ANDIE that comments on any action made by the user
Is internationalised

### ANDIE Manual && Welcome Message

A welcome message as been added to make UI experience smoother (can be disabled), and it links to the ANDIE manual that has all the infomation required to use the image editor as a pro.
You can also access the manual from the settings tab.  
Note - the manual is hosted on GitLab pages.

## Known bugs

## Refactoring

### Internationalisation

ANDIE's provided ui has been internationalised and an I18N manager has been added.

### Save on exit functionality

The EditableImage class has been altered to include an isSaved boolean to check for if the latest changes to the image have been saved before the program exits. As a result, the default close operation has been changed to be _DO_NOTHING_ON_CLOSE_. Thus anything that would usually exit the program must be modifided to send a Window_Closing flag so the windowListener registers the close.

### Shorcuts:

File:
- open: ctrl/cmd + o
- save: ctrl/cmd + s
- save as: ctrl/cmd + shift + s
- export: ctrl/cmd + shift + e
- exit: ctrl/cmd + q

Edit:
- undo: ctrl/cmd + z
- redo: ctrl/cmd + shift + z
- resize: ctrl/cmd + shift + r
- flip horizontally: ctrl/cmd + shift + h
- flip vertically: ctrl/cmd + shift + v
- rotate 90 degrees clockwise: ctrl/cmd + shift + right
- rotate 90 degrees anti-clockwise: ctrl/cmd + shift + left
- rotate 180 degrees: ctrl/cmd + shift + down
            
View:
- zoom in: ctrl/cmd + = (+)
- zoom out: ctrl/cmd + -
- zoom full: ctrl/cmd + shift + f

Filter:
- mean: ctrl/cmd + shift + m
- sharpen: ctrl/cmd + shift + k
- gaussian: ctrl/cmd + shift + g
- median: ctrl/cmd + shift + d
- emboss: ctrl/cmd + shift + e
- sobel: ctrl/cmd + shift + b
- contrast mask: ctrl/cmd + shift + t
- FIR: ctrl/cmd + Shift + j
- Random Scattering: ctrl/cmd + Shift + n

Macro:
- start recording: ctrl/cmd + shift + q
- stop recording: ctrl/cmd + shift + a
- load macro: ctrl/cmd + shift + l

Settings:
- language ctrl/cmd + shift + i
- Pop up: ctrl/cmd + shift + p
- Documentation: ctrl/cmd + shift + u
