#include <iostream>
#include <cstring>
#include <cmath>
#include "RGBImage.hpp"

using namespace std;


// assing values
RGBImage::RGBImage(){
    this->width = 0;
    this->height = 0;
    this->max_luminocity = 255;
}

// Copy constructor
RGBImage::RGBImage(const RGBImage& img){
    int height = img.getHeight();
    int width = img.getWidth();

    this->color_img = new RGBPixel*[height];
    for (int i=0; i < height; i++){
        this->color_img[i] = new RGBPixel[width];
        for (int j=0; j < width; j++){
            this->color_img[i][j] = img.color_img[i][j];
        }
    }
}

RGBImage::RGBImage(std::istream& stream){
    int width = 0, height = 0, lum = 0;
    
    stream >> width >> height >> lum;
    


    color_img = new RGBPixel*[height];
    for (int i=0; i < height; i++) {
        color_img[i] = new RGBPixel[width];
    }

    for (int row = 0; row < height; row++){
        for (int col = 0; col < width;  col++) {
             int red, green, blue;

            // Read the RGB values from the input stream
            stream >> red >> green >> blue;

            // Set the RGB values in the color_img array directly
            color_img[row][col].setRed(red);
            color_img[row][col].setGreen(green);
            color_img[row][col].setBlue(blue);
        }
    }
    

    this->height = height;
    this->width = width;
    this->max_luminocity = lum;
}

RGBPixel** RGBImage::getImage() const {
    return color_img;
}


// RGBImage::~RGBImage() {
//     for (int i = 0; i < height; i++) {
//         delete[] color_img[i];
//     }
//     delete[] color_img;
// }


Image& RGBImage::operator+=(int value){
    if (value < 0) {
        value = abs(value) + 2;
    }
    int rotations = value % 4;  // Reduce the number of rotations to [0, 3]

    if (rotations == 0) {
        return *this;
    }

    RGBPixel** new_color_img;
    if (rotations != 2) {
        new_color_img = new RGBPixel*[width];
        for (int newRow = 0; newRow < width; newRow++) {
            new_color_img[newRow] = new RGBPixel[height];
            for (int newCol = 0; newCol < height; newCol++) {
                if (rotations == 1) {
                    new_color_img[newRow][newCol] = color_img[height - newCol - 1][newRow];
                } else if (rotations == 3) {
                    new_color_img[newRow][newCol] = color_img[newCol][width - newRow - 1];
                }
            }
        }
    } else {
        new_color_img = new RGBPixel*[height];
        for (int newRow = 0; newRow < height; newRow++) {
            new_color_img[newRow] = new RGBPixel[width];
            for (int newCol = 0; newCol < width; newCol++) {
                new_color_img[newRow][newCol] = color_img[height - newRow - 1][width - newCol - 1];
            }
        }
    }


    for (int i = 0; i < height; i++) {
        delete[] color_img[i];
    }
    delete[] color_img;

    // Swap dimensions if necessary
    if (rotations % 2 == 1) {
        int temp = height;
        height = width;
        width = temp;
    }

    // Update the pixel data
    color_img = new_color_img;
    
    return *this;
}
        
Image& RGBImage::operator *= (double factor){
    int newHeight = static_cast<int>(floor(height * factor));
    int newWidth = static_cast<int>(floor(width * factor));

    RGBPixel** new_color_img = new RGBPixel*[newHeight];
    for (int newRow = 0; newRow < newHeight; newRow++) {
        new_color_img[newRow] = new RGBPixel[newWidth];
        for (int newCol = 0; newCol < newWidth; newCol++) {
            int r1 = min(static_cast<int>(floor(newRow / factor)), height - 1);
            int r2 = min(static_cast<int>(ceil(newRow / factor)), height - 1);
            int c1 = min(static_cast<int>(floor(newCol / factor)), width - 1);
            int c2 = min(static_cast<int>(ceil(newCol / factor)), width - 1);

            int r_value = (color_img[r1][c1].getRed() + color_img[r1][c2].getRed() +
                         color_img[r2][c1].getRed() + color_img[r2][c2].getRed()) / 4;
            int g_value = (color_img[r1][c1].getGreen() + color_img[r1][c2].getGreen() +
                         color_img[r2][c1].getGreen() + color_img[r2][c2].getGreen()) / 4;
            int b_value = (color_img[r1][c1].getBlue() + color_img[r1][c2].getBlue() +
                         color_img[r2][c1].getBlue() + color_img[r2][c2].getBlue()) / 4;

            new_color_img[newRow][newCol].setRed(r_value);
            new_color_img[newRow][newCol].setGreen(g_value);
            new_color_img[newRow][newCol].setBlue(b_value);
            
        }
    }

    // Free the memory of the original bw_pixel
    for (int i = 0; i < height; i++) {
        delete[] color_img[i];
    }
    delete[] color_img;

    // Update the image dimensions and pixel data
    height = newHeight;
    width = newWidth;
    color_img = new_color_img;

    return *this;  // Return a reference to the modified object
}

Image& RGBImage::operator !(){

    for (int row = 0; row < height; row++) {
        for (int col = 0; col < width; col++) {
            int red = color_img[row][col].getRed();
            int green = color_img[row][col].getGreen();
            int blue = color_img[row][col].getBlue();

            color_img[row][col].setRed(255-red);
            color_img[row][col].setGreen(255-green);
            color_img[row][col].setBlue(255-blue);
        }
    }

    return *this;
}


Image& RGBImage::operator ~(){
    int max_lum = 236;
    double* histogram = new double[max_lum];

    for(int i=0; i < max_lum; i++){
        histogram[i] = 0;
    }

    for(int i=0; i<height; i++) {
        for(int j=0; j<width; j++) {
            for(int k=0; k<max_lum; k++) {
                if(calc_y(color_img[i][j].getRed(), color_img[i][j].getGreen(), color_img[i][j].getBlue()) == k) {
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

    int y[height][width] = {0};
    int u[height][width] = {0};
    int v[height][width] = {0};

    for (int i=0; i < height; i++){
        for(int j = 0; j < width; j++){
            y[i][j] = histogram[calc_y(color_img[i][j].getRed(), color_img[i][j].getGreen(), color_img[i][j].getBlue())];
            u[i][j] = calc_u(color_img[i][j].getRed(), color_img[i][j].getGreen(), color_img[i][j].getBlue());
            v[i][j] = calc_v(color_img[i][j].getRed(), color_img[i][j].getGreen(), color_img[i][j].getBlue());
        }
    }

    for (int i=0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            color_img[i][j].setRed(calc_r(y[i][j], u[i][j], v[i][j]));
            color_img[i][j].setGreen(calc_g(y[i][j], u[i][j], v[i][j]));
            color_img[i][j].setBlue(calc_b(y[i][j], u[i][j], v[i][j]));
        }
    }
    delete[] histogram;
    return *this;
}

Image& RGBImage::operator *(){
   
    int lhs = floor(width/2);
    
    for (int row = 0; row < height; row++) {
        for(int col = 0; col < lhs; col++) {
            swap(color_img[row][col], color_img[row][width-col-1]);
        }
    }
    return *this;
}
Pixel& RGBImage::getPixel(int row, int col) const{
    return this->color_img[row][col];
}



