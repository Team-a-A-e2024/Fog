package app.service;

import app.entities.Material;
import app.entities.Partslist;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SvgTest {

    @BeforeAll
    public static void beforeAll() {
    }

    @BeforeEach
    void beforeEach() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void TestSvg() {
        //arrange
        String expected = "<svg version=\"1.1\"\n" +
                "     x=\"0\" y=\"0\"\n" +
                "     viewBox=\"\" \n" +
                "     preserveAspectRatio=\"xMinYMin\"><defs>\n" +
                "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "    </defs></svg>";
        //act
        String actual = new Svg(0,0,"").toString();
        //assert
        assertEquals(expected,actual);
    }

    @Test
    void testAddRectangle() {
        //arrange
        String expected = "<svg version=\"1.1\"\n" +
                "     x=\"0\" y=\"0\"\n" +
                "     viewBox=\"\" \n" +
                "     preserveAspectRatio=\"xMinYMin\"><defs>\n" +
                "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "    </defs><rect x=\"1.0\" y=\"1.0\" height=\"1.0\" width=\"1.0\" style=\"\" /></svg>";
        //act
        Svg svg = new Svg(0,0,"");
        svg.addRectangle(1,1,1,1,"");
        String actual = svg.toString();
        //assert
        assertEquals(expected,actual);
    }

    @Test
    void testAddLine() {
        //arrange
        String expected = "<svg version=\"1.1\"\n" +
                "     x=\"0\" y=\"0\"\n" +
                "     viewBox=\"\" \n" +
                "     preserveAspectRatio=\"xMinYMin\"><defs>\n" +
                "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "    </defs><line x1=\"1\" y1=\"1\" x2=\"1\" y2=\"1\" style=\"\" /></svg>";
        //act
        Svg svg = new Svg(0,0,"");
        svg.addLine(1,1,1,1,"");
        String actual = svg.toString();
        //assert
        assertEquals(expected,actual);
    }

    @Test
    void testAddArrow() {
        //arrange
        String expected = "<svg version=\"1.1\"\n" +
                "     x=\"0\" y=\"0\"\n" +
                "     viewBox=\"\" \n" +
                "     preserveAspectRatio=\"xMinYMin\"><defs>\n" +
                "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "    </defs><line x1=\"1\" y1=\"1\" x2=\"1\" y2=\"1\" style=\"\"  marker-end=\"url(#endArrow)\" marker-start=\"url(#beginArrow)\"/></svg>";
        //act
        Svg svg = new Svg(0,0,"");
        svg.addArrow(1,1,1,1,"");
        String actual = svg.toString();
        //assert
        assertEquals(expected,actual);
    }

    @Test
    void testAddText() {
        //arrange
        String expected = "<svg version=\"1.1\"\n" +
                "     x=\"0\" y=\"0\"\n" +
                "     viewBox=\"\" \n" +
                "     preserveAspectRatio=\"xMinYMin\"><defs>\n" +
                "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "    </defs><text x=\"1.0\" y=\"1.0\" text-anchor=\"middle\" dominant-baseline=\"central\" transform=\"rotate(1 1.0,1.0)\" style=\"\">testy</text></svg>";
        //act
        Svg svg = new Svg(0,0,"");
        svg.addText(1,1,1,"","testy");
        String actual = svg.toString();
        //assert
        assertEquals(expected,actual);
    }

    @Test
    void testAddSvg() {
        //arrange
        String expected = "<svg version=\"1.1\"\n" +
                "     x=\"0\" y=\"0\"\n" +
                "     viewBox=\"\" \n" +
                "     preserveAspectRatio=\"xMinYMin\"><defs>\n" +
                "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "    </defs><svg version=\"1.1\"\n" +
                "     x=\"0\" y=\"0\"\n" +
                "     viewBox=\"\" \n" +
                "     preserveAspectRatio=\"xMinYMin\"><defs>\n" +
                "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
                "        </marker>\n" +
                "    </defs></svg></svg>";
        //act
        Svg svg = new Svg(0,0,"");
        svg.addSvg(new Svg(0,0,""));
        String actual = svg.toString();
        //assert
        assertEquals(expected,actual);
    }


}
