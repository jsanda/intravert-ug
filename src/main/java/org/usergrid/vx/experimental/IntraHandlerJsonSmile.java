/* 
 *   Copyright 2013 Nate McCall and Edward Capriolo
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
*/
package org.usergrid.vx.experimental;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.smile.SmileFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
/**
 * <img src="http://www.michaeljackson.com/sites/mjackson/files/photos/Michael-Jackson-Smile-Sunglasses.jpg">
 * @author edward
 *
 */
public class IntraHandlerJsonSmile implements Handler<HttpServerRequest>{
	static IntraService is = new IntraService();
	static SmileFactory sf = new SmileFactory();
	static ObjectMapper mapper = new ObjectMapper(sf);
  private Vertx vertx;
  
  public IntraHandlerJsonSmile(Vertx vertx){
    this.vertx=vertx;
  }

	@Override
	public void handle(final HttpServerRequest request) {
		final IntraRes res = new IntraRes();
		request.bodyHandler( new Handler<Buffer>() {
			public void handle(Buffer buffer) {

				IntraReq req = null;
				try {
					req = mapper.readValue(buffer.getBytes(), IntraReq.class);
	         //System.out.println("Mapped req");
	         // System.out.println(req.getE());
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				is.handleIntraReq(req,res,vertx);
				
				byte [] value=null;
				try {
					value = mapper.writeValueAsBytes(res);
					//ystem.out.println(value);
				} catch (JsonGenerationException e1) {
					e1.printStackTrace();
				} catch (JsonMappingException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				request.response.end( new Buffer(value));
				//System.out.println("sent back buffer");
				//request.response.write(new Buffer(value));
			}
		});
	}
}
