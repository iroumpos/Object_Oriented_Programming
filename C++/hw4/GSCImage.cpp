#include <iostream>
#include <fstream>
#include <cmath>
#include "GSCImage.hpp"
#include "RGBImage.hpp"

using namespace std;


// assing values
GSCImage::GSCImage(){
    this->width = 0;
    this->height = 0;
    this->max_luminocity = 255;
}

// Copy constructor
GSCImage::GSCImage(const GSCImage& img){
    int height = img.getHeight();
    int width = img.getWidth();

    this->bw_pixel = new GSCPixel*[height];
    for (int i=0; i < height; i++){
        this->bw_pixel[i] = new GSCPixel[width];
        for (int j=0; j < width; j++){
            this->bw_pixel[i][j] = img.bw_pixel[i][j];
        }
    }
}

// Create a grayscale image from coloured one
GSCImage::GSCImage(const RGBImage& grayscaled){
    int height = grayscaled.getHeight();
    int width = grayscaled.getWidth();
    int max_lum = grayscaled.getMaxLuminocity();

    bw_pixel = new GSCPixel*[height];
    for (int i = 0; i < height; i++) {
        bw_pixel[i] = new GSCPixel[width];
        for (int j=0; j < width; j++){
           unsigned char value = grayscaled.getImage()[i][j].getRed() * 0.3 + grayscaled.getImage()[i][j].getGreen() * 0.59 + grayscaled.getImage()[i][j].getBlue() * 0.11;
           bw_pixel[i][j] = GSCPixel(value);
        }
    }

    this->height = height;
    this->width = width;
    this->max_luminocity = max_lum;
}

GSCImage::GSCImage(std::istream& stream){
    int width = 0, height = 0, lum = 0;
    
    stream >> width >> height >> lum;
    

    bw_pixel = new GSCPixel*[height];
    for (int i=0; i < height; i++) {
        bw_pixel[i] = new GSCPixel[width];
    }

    for (int row = 0; row < height; row++){
        for (int col = 0; col < width;  col++) {
             int gsc_pixel;

            // Read the GSC value from the input stream
            stream >> gsc_pixel;

            // Set the GSC values in the bw_pixel array directly
            bw_pixel[row][col] = GSCPixel(gsc_pixel);
        }
    }
    

    this->height = height;
    this->width = width;
    this->max_luminocity = lum;
}


Image& GSCImage::operator+=(int value){
    
    if (value < 0) {
        value = abs(value) + 2;
    }
    int rotations = value % 4;  // Reduce the number of rotations to [0, 3]

    if (rotations == 0) {
        return *this;
    }

    GSCPixel** new_bw_pixel;
    if (rotations != 2) {
        new_bw_pixel = new GSCPixel*[width];
        for (int newRow = 0; newRow < width; newRow++) {
            new_bw_pixel[newRow] = new GSCPixel[height];
            for (int newCol = 0; newCol < height; newCol++) {
                if (rotations == 1) {
                    new_bw_pixel[newRow][newCol] = bw_pixel[height - newCol - 1][newRow];
                } else if (rotations == 3) {
                    new_bw_pixel[newRow][newCol] = bw_pixel[newCol][width - newRow - 1];
                }
            }
        }
    } else {
        new_bw_pixel = new GSCPixel*[height];
        for (int newRow = 0; newRow < height; newRow++) {
            new_bw_pixel[newRow] = new GSCPixel[width];
            for (int newCol = 0; newCol < width; newCol++) {
                new_bw_pixel[newRow][newCol] = bw_pixel[height - newRow - 1][width - newCol - 1];
            }
        }
    }



    // Free the memory of the original bw_pixel
    for (int i = 0; i < height; i++) {
        delete[] bw_pixel[i];
    }
    delete[] bw_pixel;

    // Swap dimensions if necessary
    if (rotations % 2 == 1) {
        int temp = height;
        height = width;
        width = temp;
    }

    // Update the pixel data
    bw_pixel = new_bw_pixel;

    return *this;
}
        
Image& GSCImage::operator*=(double factor) {
    int newHeight = static_cast<int>(floor(height * factor));
    int newWidth = static_cast<int>(floor(width * factor));

    GSCPixel** new_bw_pixel = new GSCPixel*[newHeight];
    for (int newRow = 0; newRow < newHeight; newRow++) {
        new_bw_pixel[newRow] = new GSCPixel[newWidth];
        for (int newCol = 0; newCol < newWidth; newCol++) {
            int r1 = min(static_cast<int>(floor(newRow / factor)), height - 1);
            int r2 = min(static_cast<int>(ceil(newRow / factor)), height - 1);
            int c1 = min(static_cast<int>(floor(newCol / factor)), width - 1);
            int c2 = min(static_cast<int>(ceil(newCol / factor)), width - 1);

            int value = (bw_pixel[r1][c1].getValue() + bw_pixel[r1][c2].getValue() +
                         bw_pixel[r2][c1].getValue() + bw_pixel[r2][c2].getValue()) / 4;

            new_bw_pixel[newRow][newCol] = GSCPixel(value);
        }
    }

    // Free the memory of the original bw_pixel
    for (int i = 0; i < height; i++) {
        delete[] bw_pixel[i];
    }
    delete[] bw_pixel;

    // Update the image dimensions and pixel data
    height = newHeight;
    width = newWidth;
    bw_pixel = new_bw_pixel;

    return *this;  // Return a reference to the modified object
}


Image& GSCImage::operator !(){

    for (int row = 0; row < height; row++) {
        for (int col = 0; col < width; col++) {
            int value = bw_pixel[row][col].getValue();
            
            bw_pixel[row][col].setValue(255-value);
           
        }
    }

    return *this;
}


Image& GSCImage::operator ~(){
    int max_lum = 236;
    double* histogram = new double[max_lum];

    for(int i=0; i < max_lum; i++){
        histogram[i] = 0;
    }

    for(int i=0; i<height; i++) {
        for(int j=0; j<width; j++) {
            for(int k=0; k<max_lum; k++) {
                if(calc_y(bw_pixel[i][j].getValue(), bw_pixel[i][j].getValue(), bw_pixel[i][j].getValue()) == k) {
                    histogram[k]++;
                    break;
                }
            }
        }
    }
    double sum = 0;
    for (int i=0; i < max_lum; i++) {
        histogram[i] = histogram[i]/(height*width);
    }

    for (int i=0; i < max_lum; i++) {
        sum += histogram[i];
        histogram[i] = sum;
    }

    for (int i=0; i < max_lum; i++) {
        histogram[i] = 235*histogram[i];
    }

    // for (int i=0; i < max_lum; i++) {
    //     cout << histogram[i] << endl;
    // }
    int y[height][width] = {0};

    for (int i=0; i < height; i++){
        for(int j = 0; j < width; j++){
            y[i][j] = histogram[calc_y(bw_pixel[i][j].getValue(), bw_pixel[i][j].getValue(), bw_pixel[i][j].getValue())];
        }
    }

    GSCPixel** new_bw_pixel = new GSCPixel*[height];
    for (int i = 0; i < height; i++) {
        new_bw_pixel[i] = new GSCPixel[width];
        for(int j = 0; j < width; j++){
            int value = grey_to_y(y[i][j]);
            new_bw_pixel[i][j] = GSCPixel(value);
        }
    }
    // Free the memory of the original bw_pixel
    for (int i = 0; i < height; i++) {
        delete[] bw_pixel[i];
    }
    delete[] bw_pixel;

    bw_pixel = new_bw_pixel;

    
    delete[] histogram;
    return *this;
}
Image& GSCImage::operator *(){

    int lhs = floor(width/2);
    
    for (int row = 0; row < height; row++) {
        for(int col = 0; col < lhs; col++) {
            swap(bw_pixel[row][col], bw_pixel[row][width-col-1]);
        }
    }
    return *this;

}
Pixel& GSCImage::getPixel(int row, int col) const {
    return bw_pixel[row][col];
}


// GSCImage::~GSCImage() {
//     for (int i = 0; i < height; i++) {
//         delete[] bw_pixel[i];
//     }
//     delete[] bw_pixel;
// }
