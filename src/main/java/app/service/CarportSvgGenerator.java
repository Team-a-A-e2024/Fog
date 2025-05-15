package app.service;

import java.util.Vector;

public class CarportSvgGenerator {

    private Svg svg;

    private static final int sidemargin = 35;
    private static final int frontmargin = 100;
    private static final int backmargin = 30;
    private static final float poleSize = 9.7f;
    private static final float rafterwidth = 4.5f;
    private static final int maxDistancebetweenposts = PostCalculationStrategy.maxDistanceBetweenPosts;
    private static final int postAtBeamExtension = PostCalculationStrategy.postAtBeamExtension;
    private static final int rafterSpacing = RaftersCalculationStrategy.rafterSpacing;
    private static final int offsetX = 100;
    private static final int offsetY = 100;
    private static final int margin = 100;

    //size of the carport, 1 = 1cm
    public CarportSvgGenerator(int x, int y, int width, int length){
        svg = new Svg(x,y,"0 0 " + (length + offsetX + margin) + " " + (width + offsetY + margin) + "\"" ,"100%");
        drawTopPoles(width,length);
        drawTopBeams(width,length);
        drawTopRafters(width,length);
        drawArrowWithText(-50,0,-50, width,"" + width);
        drawArrowWithText(0,width+50, length, width+50,"" + length);
    }

    public void drawTopPoles(int width, int length){
        drawTopSinglePole(frontmargin, sidemargin);
        drawTopSinglePole(frontmargin, width-sidemargin);
        drawTopSinglePole(length-backmargin, sidemargin);
        drawTopSinglePole(length-backmargin, width-sidemargin);

        //arrow
        drawArrowWithText(-20,sidemargin,-20, width-sidemargin,"" + (width-sidemargin-sidemargin));
        drawArrowWithText(0,width+30,frontmargin, width+30,"" + frontmargin);
        drawArrowWithText(length - backmargin,width+30,length, width+30,"" + backmargin);

        float deltaLength = length-frontmargin-backmargin;

        if(length-backmargin > postAtBeamExtension){
            drawTopSinglePole(postAtBeamExtension, sidemargin);
            drawTopSinglePole(postAtBeamExtension, width-sidemargin);
            drawTopSinglePole(postAtBeamExtension/2, sidemargin);
            drawTopSinglePole(postAtBeamExtension/2, width-sidemargin);

            //arrows
            drawArrowWithText(frontmargin,width+20,postAtBeamExtension/2, width+20,"" + 350);
            drawArrowWithText(postAtBeamExtension/2,width+20,postAtBeamExtension, width+20,"" + 350);
            drawArrowWithText(postAtBeamExtension,width+20,length - backmargin, width+20,"" + (length - postAtBeamExtension - backmargin));
            return;
        }
        if(deltaLength > maxDistancebetweenposts){
            float halfDelta = (1f/2f)*(deltaLength);

            drawTopSinglePole((int)halfDelta+frontmargin, sidemargin);
            drawTopSinglePole((int)halfDelta+frontmargin, width-sidemargin);

            //arrows
            drawArrowWithText(frontmargin,width+20,frontmargin + (int)halfDelta, width+20,"" + halfDelta);
            drawArrowWithText(frontmargin + (int)halfDelta,width+20,length-backmargin, width+20,"" + halfDelta);
            return;
        }
        //arrow
        drawArrowWithText(frontmargin,width+20,length-backmargin, width+20,"" + deltaLength);
    }

    public void drawTopSinglePole(int x, int y){
        svg.addRectangle(x - poleSize/2 + offsetX,y - poleSize/2 + offsetY,poleSize,poleSize,"stroke:black; stroke-width:1; fill:white;");
    }

    public void drawTopBeams(int width, int length){
        if(length-backmargin > postAtBeamExtension){
            int deltalength = length - postAtBeamExtension;
            drawTopSingleBeam(postAtBeamExtension, sidemargin, deltalength);
            drawTopSingleBeam(postAtBeamExtension, width-sidemargin, deltalength);
            drawTopSingleBeam(0, sidemargin, postAtBeamExtension);
            drawTopSingleBeam(0, width-sidemargin, postAtBeamExtension);
            return;
        }
        drawTopSingleBeam(0, sidemargin, length);
        drawTopSingleBeam(0, width-sidemargin, length);
    }

    public void drawTopSingleBeam(float x, float y, int length){
        svg.addRectangle(x + offsetX,y + offsetY,rafterwidth,length,"stroke:black; stroke-width:1; fill:none;");
    }

    public void drawTopRafters(int width, int length){

        int count = length/rafterSpacing;

        for (int i = 1; i <= count; i++){
            drawTopSingleRafter((int)(((float)i/(float)count)*(float)length), width);
            drawArrowWithText((int)(((float)(i-1)/(float)count)*(float)length),-10,(int)(((float)i/(float)count)*(float)length),-10,rafterSpacing +"");
        }
        drawTopSingleRafter(0, width);
        drawTopSingleRafter(length, width);
    }

    public void drawTopSingleRafter(int x, int width){
        svg.addRectangle(x + offsetX, offsetY, width, rafterwidth,"stroke:black; stroke-width:1; fill:white;");
    }

    public Svg getSvg() {
        return svg;
    }

    public void setSvg(Svg svg) {
        this.svg = svg;
    }

    public void drawArrowWithText(int startX, int startY, int endX, int endY, String text){
        svg.addArrow(startX + offsetX, startY + offsetY, endX + offsetX, endY + offsetY,"stroke:black; stroke-width:1;");

        //vector
        float x1 = endX-startX;
        float y1 = endY-startY;

        //lengths
        float v1Length = (float) Math.sqrt((x1*x1 + y1*y1));
        double angle = Math.toDegrees(Math.acos((x1)/(v1Length)));

        //vector normalized
        float x2 = x1/v1Length;
        float y2 = y1/v1Length;

        svg.addText((float) (x1)/2f + startX + offsetX + (-y2 * 10),(float) (y1)/2f + startY + offsetX + (x2 * 10),- (int)angle,"color:black;",text);
    }
}
