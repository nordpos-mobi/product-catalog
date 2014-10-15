/**
 * Copyright (c) 2012-2014 Nord Trading Network.
 */
package mobi.nordpos.catalog.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import mobi.nordpos.catalog.ext.Public;
import mobi.nordpos.catalog.model.Product;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
@Public
public class ProductBarcodeActionBean extends ProductBaseActionBean {

    private static final String PRODUCT = "/WEB-INF/jsp/product_view.jsp";

    private static final int WIDTH = 256;
    private static final int HEIGH = 128;
    private static final int SCALE = 2;

    protected AbstractBarcodeBean barcodeBean;

    @DefaultHandler
    public StreamingResolution ean13() throws IOException, SQLException {
        return new StreamingResolution("image/png") {
            @Override
            public void stream(HttpServletResponse response) throws Exception {
                String code = getProduct().getCode();
                barcodeBean = new EAN13Bean();
                BufferedImage image = new BufferedImage(WIDTH, HEIGH, BufferedImage.TYPE_BYTE_BINARY);
                if (barcodeBean != null && new EAN13CheckDigit().isValid(code)) {
                    barcodeBean.setModuleWidth(1.0);
                    barcodeBean.setBarHeight(HEIGH / 4);
                    barcodeBean.setFontSize(10.0);
                    barcodeBean.setQuietZone(10.0);
                    barcodeBean.doQuietZone(true);

                    barcodeBean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);

                    BarcodeDimension dimension = barcodeBean.calcDimensions(code);
                    
                    Graphics2D g2d = image.createGraphics();
                    g2d.setBackground(Color.WHITE);
                    g2d.clearRect(0, 0, WIDTH, HEIGH);
                    g2d.setColor(Color.BLACK);

                    g2d.translate(10, 10);

                    g2d.scale(SCALE, SCALE);

                    barcodeBean.generateBarcode(new Java2DCanvasProvider(g2d, 0), code);
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                baos.flush();
                response.getOutputStream().write(baos.toByteArray());
                response.flushBuffer();
                baos.close();
            }
        }.setFilename("EAN13-".concat(getProduct().getCode()).concat(".png")).setAttachment(true);
    }

    @ValidateNestedProperties({
        @Validate(field = "code",
                required = true,
                trim = true,
                minlength = 13,
                maxlength = 13,
                mask = "[0-9]+")
    })
    @Override
    public void setProduct(Product product) {
        super.setProduct(product);
    }

}
