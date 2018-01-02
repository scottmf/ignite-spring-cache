/**
 * Copyright (C) 2017 Scott Feldstein
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.scottieknows.ignite.springcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/hello")
public class CachingController {

    @Autowired
    private CachingService cachingService;

    @GetMapping
    public ResponseEntity<String> hello(@RequestParam("id") int id) {
        String rtn = cachingService.getById(id);
        return new ResponseEntity<String>(rtn, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<String> patch(@RequestBody HelloRequest helloRequest) {
        cachingService.put(helloRequest.getId(), helloRequest.getValue());
        String rtn = cachingService.getById(helloRequest.getId());
        return new ResponseEntity<String>(rtn, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> post(@RequestBody HelloRequest helloRequest) {
        cachingService.put(helloRequest.getId(), helloRequest.getValue());
        String rtn = cachingService.getById(helloRequest.getId());
        return new ResponseEntity<String>(rtn, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> put(@RequestBody HelloRequest helloRequest) {
        cachingService.put(helloRequest.getId(), helloRequest.getValue());
        String rtn = cachingService.getById(helloRequest.getId());
        return new ResponseEntity<String>(rtn, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("id") int id) {
        cachingService.removeById(id);
        return new ResponseEntity<String>("deleted", HttpStatus.OK);
    }

}
