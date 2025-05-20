package app.service;

public class Svg
{
    private static final String SVG_TEMPLATE = "<svg version=\"1.1\"\n" +
            "     x=\"%d\" y=\"%d\"\n" +
            "     viewBox=\"%s\" \n" +
            "     preserveAspectRatio=\"xMinYMin\">";

    private static final String SVG_ARROW_DEFS = "<defs>\n" +
            "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "    </defs>";

    private static final String SVG_RECT_TEMPLATE = "<rect x=\"%s\" y=\"%s\" height=\"%s\" width=\"%s\" style=\"%s\" />";
    private static final String SVG_LINE_TEMPLATE = "<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"%s\" />";
    private static final String SVG_ARROW_TEMPLATE = "<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"%s\"  marker-end=\"url(#endArrow)\" marker-start=\"url(#beginArrow)\"/>";
    private static final String SVG_TEXT_TEMPLATE = "<text x=\"%s\" y=\"%s\" text-anchor=\"middle\" dominant-baseline=\"central\" transform=\"rotate(%d %s,%s)\" style=\"%s\">%s</text>";


    private StringBuilder svg = new StringBuilder();

    public Svg(int x, int y, String viewBox)
    {
        svg.append(String.format(SVG_TEMPLATE, x, y, viewBox));
        svg.append(SVG_ARROW_DEFS);
    }

    public void addRectangle(float x, float y, float height, float width, String style)
    {
        svg.append(String.format(SVG_RECT_TEMPLATE, Float.toString(x), Float.toString(y), Float.toString(height), Float.toString(width), style ));
    }


    public void addLine(int x1, int y1, int x2, int y2, String style)
    {
        svg.append(String.format(SVG_LINE_TEMPLATE, x1, y1, x2, y2, style ));
    }

    public void addArrow(int x1, int y1, int x2, int y2, String style)
    {
        svg.append(String.format(SVG_ARROW_TEMPLATE, x1, y1, x2, y2, style ));
    }

    public void addText(float x, float y, int rotation,  String style, String text)
    {
        svg.append(String.format(SVG_TEXT_TEMPLATE, Float.toString(x), Float.toString(y), rotation, Float.toString(x), Float.toString(y), style, text));
    }

    public void addSvg(Svg innerSvg)
    {
        svg.append(innerSvg.toString());
    }

    @Override
    public String toString()
    {
        return svg.append("</svg>").toString();
    }
}
