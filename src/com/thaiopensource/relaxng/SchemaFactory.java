package com.thaiopensource.relaxng;

import org.relaxng.datatype.DatatypeLibraryFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;

public class SchemaFactory {
  private XMLReaderCreator xrc = null;
  private ErrorHandler eh = null;
  private DatatypeLibraryFactory dlf = null;
  private boolean checkIdIdref = false;

  public SchemaFactory() {
  }

  public Schema createSchema(InputSource in) throws IOException, SAXException, IncorrectSchemaException {
    SchemaPatternBuilder spb = new SchemaPatternBuilder();
    XMLReader xr = xrc.createXMLReader();
    if (eh != null)
      xr.setErrorHandler(eh);
    Pattern start = PatternReader.readPattern(xrc, xr, spb, dlf, in);
    if (start == null)
      throw new IncorrectSchemaException();
    Schema schema = new PatternSchema(spb, start);
    if (spb.hasIdTypes() && checkIdIdref) {
      IdTypeMap idTypeMap = new IdTypeMapBuilder(xr, start).getIdTypeMap();
      if (idTypeMap == null)
        throw new IncorrectSchemaException();
      schema = new CombineSchema(schema, new IdTypeMapSchema(idTypeMap));
    }
    return schema;
  }

  public void setXMLReaderCreator(XMLReaderCreator xrc) {
    this.xrc = xrc;
  }

  public XMLReaderCreator getXMLReaderCreator(XMLReaderCreator xrc) {
    return xrc;
  }

  public void setErrorHandler(ErrorHandler eh) {
    this.eh = eh;
  }

  public ErrorHandler getErrorHandler() {
    return eh;
  }

  public void setDatatypeLibraryFactory(DatatypeLibraryFactory dlf) {
    this.dlf = dlf;
  }

  public DatatypeLibraryFactory getDatatypeLibraryFactory() {
    return dlf;
  }

  public void setCheckIdIdref(boolean checkIdIdref) {
    this.checkIdIdref = checkIdIdref;
  }

  public boolean getCheckIdIdref() {
    return checkIdIdref;
  }
}
