package app.service;

public class CarportSvgGenerator {

    private Svg svg;

    //size of the carport, 1 = 1cm
    public CarportSvgGenerator(int x, int y, int lengthcm, int widthcm){
        svg = new Svg(x,y,"","100%");
        drawTopPoles(lengthcm,widthcm);

    }

    public void drawTopPoles(int width, int length){
        drawTopSinglePole(100,100);
        drawTopSinglePole(width-100,100);
        drawTopSinglePole(100,length-50);
        drawTopSinglePole(width-100,length-50);
        if (width-150 > 600){
            drawTopSinglePole(600,100);
            drawTopSinglePole(600,length-50);
        }
        if (length-150 > 600){
            drawTopSinglePole(100,600);
            drawTopSinglePole(width-100,600);
        }
    }

    public void drawTopSinglePole(int x, int y){
        svg.addRectangle(x,y,9.7f,9.7f,"stroke:black; stroke-width:1; fill:white;");
    }

    public void drawTopBeams(int width, int length){

    }

    public void drawTopRafters(int width, int length){

    }

    public void drawTopSingleRafter(int x, int y, int width){
        svg.addRectangle(x,y,5,width,"stroke=black stroke-width=1");
    }

    public Svg getSvg() {
        return svg;
    }

    public void setSvg(Svg svg) {
        this.svg = svg;
    }
}
