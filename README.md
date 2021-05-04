# PhotoLister-Demo

This program helps to find photos that contains given words on anywhere of its pixels, for a given list of photos using Tesseract OCR.

To run this program, Tesseract OCR must be installed and added to the path, and also installed location must be re-configured in the code.

In "test files" folder, add photos to "photos to be searched" folder and run the program.

The program will start searching photos in that directory, writing all the text containing to "text files" directory, spesifying which text file owned by which photo, also look
for a match for the given words, and if there is a match, put the matched photo to "original photos" and "photos containing the text" folder. While doing these matches, total 
4 extra photos around the matched photo will be added to "photos containing the text" folder.

The reason behind that is generally sequential photos have a great chance to be in nearly same context, so while reading texts, in case of a failure of matching the text, and
moving forward and matching the next one, the previous failed matched will be restored too.

After the program finishes, you can check original photos and original photos with some neighbors. Howmuch neighbor will be fetched extra can be determined by changing lower
and upper bounds.

