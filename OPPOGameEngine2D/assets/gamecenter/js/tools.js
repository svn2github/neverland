/**
 * TrimPath Template. Release 1.0.38.
 * Copyright (C) 2004, 2005 Metaha.
 * 
 * TrimPath Template is licensed under the GNU General Public License
 * and the Apache License, Version 2.0, as follows:
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; without even the 
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var TrimPath;(function(){if(TrimPath==null)TrimPath=new Object();if(TrimPath.evalEx==null)TrimPath.evalEx=function(src){return eval(src)};var UNDEFINED;if(Array.prototype.pop==null)Array.prototype.pop=function(){if(this.length===0){return UNDEFINED};return this[--this.length]};if(Array.prototype.push==null)Array.prototype.push=function(){for(var i=0;i<arguments.length;++i){this[this.length]=arguments[i]};return this.length};TrimPath.parseTemplate=function(tmplContent,optTmplName,optEtc){if(optEtc==null)optEtc=TrimPath.parseTemplate_etc;var funcSrc=parse(tmplContent,optTmplName,optEtc);var func=TrimPath.evalEx(funcSrc,optTmplName,1);if(func!=null)return new optEtc.Template(optTmplName,tmplContent,funcSrc,func,optEtc);return null};try{String.prototype.process=function(context,optFlags){var template=TrimPath.parseTemplate(this,null);if(template!=null)return template.process(context,optFlags);return this}}catch(e){};TrimPath.parseTemplate_etc={};TrimPath.parseTemplate_etc.statementTag="forelse|for|if|elseif|else|var|macro";TrimPath.parseTemplate_etc.statementDef={"if":{delta:1,prefix:"if (",suffix:") {",paramMin:1},"else":{delta:0,prefix:"} else {"},"elseif":{delta:0,prefix:"} else if (",suffix:") {",paramDefault:"true"},"/if":{delta:-1,prefix:"}"},"for":{delta:1,paramMin:3,prefixFunc:function(stmtParts,state,tmplName,etc){if(stmtParts[2]!="in")throw new etc.ParseError(tmplName,state.line,"bad for loop statement: "+stmtParts.join(' '));var iterVar=stmtParts[1];var listVar="__LIST__"+iterVar;return["var ",listVar," = ",stmtParts[3],";","var __LENGTH_STACK__;","if (typeof(__LENGTH_STACK__) == 'undefined' || !__LENGTH_STACK__.length) __LENGTH_STACK__ = new Array();","__LENGTH_STACK__[__LENGTH_STACK__.length] = 0;","if ((",listVar,") != null) { ","var ",iterVar,"_ct = 0;","for (var ",iterVar,"_index in ",listVar,") { ",iterVar,"_ct++;","if (typeof(",listVar,"[",iterVar,"_index]) == 'function') {continue;}","__LENGTH_STACK__[__LENGTH_STACK__.length - 1]++;","var ",iterVar," = ",listVar,"[",iterVar,"_index];"].join("")}},"forelse":{delta:0,prefix:"} } if (__LENGTH_STACK__[__LENGTH_STACK__.length - 1] == 0) { if (",suffix:") {",paramDefault:"true"},"/for":{delta:-1,prefix:"} }; delete __LENGTH_STACK__[__LENGTH_STACK__.length - 1];"},"var":{delta:0,prefix:"var ",suffix:";"},"macro":{delta:1,prefixFunc:function(stmtParts,state,tmplName,etc){var macroName=stmtParts[1].split('(')[0];return["var ",macroName," = function",stmtParts.slice(1).join(' ').substring(macroName.length),"{ var _OUT_arr = []; var _OUT = { write: function(m) { if (m) _OUT_arr.push(m); } }; "].join('')}},"/macro":{delta:-1,prefix:" return _OUT_arr.join(''); };"}};TrimPath.parseTemplate_etc.modifierDef={"eat":function(v){return""},"escape":function(s){return String(s).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")},"capitalize":function(s){return String(s).toUpperCase()},"default":function(s,d){return s!=null?s:d}};TrimPath.parseTemplate_etc.modifierDef.h=TrimPath.parseTemplate_etc.modifierDef.escape;TrimPath.parseTemplate_etc.Template=function(tmplName,tmplContent,funcSrc,func,etc){this.process=function(context,flags){if(context==null)context={};if(context._MODIFIERS==null)context._MODIFIERS={};  if(window.nearmelang){if( context.nearmelang ){context.nearmelang=$.extend(true,{},context.nearmelang,window.nearmelang);}else{context.nearmelang = window.nearmelang;}}if(context.defined==null)context.defined=function(str){return(context[str]!=undefined)};for(var k in etc.modifierDef){if(context._MODIFIERS[k]==null)context._MODIFIERS[k]=etc.modifierDef[k]};if(flags==null)flags={};var resultArr=[];var resultOut={write:function(m){resultArr.push(m)}};try{func(resultOut,context,flags)}catch(e){if(flags.throwExceptions==true)throw e;var result=new String(resultArr.join("")+"[ERROR: "+e.toString()+(e.message?'; '+e.message:'')+"]");result["exception"]=e;console.log(e);return result};return resultArr.join("")};this.name=tmplName;this.source=tmplContent;this.sourceFunc=funcSrc;this.toString=function(){return"TrimPath.Template ["+tmplName+"]"}};TrimPath.parseTemplate_etc.ParseError=function(name,line,message){this.name=name;this.line=line;this.message=message};TrimPath.parseTemplate_etc.ParseError.prototype.toString=function(){return("TrimPath template ParseError in "+this.name+": line "+this.line+", "+this.message)};var parse=function(body,tmplName,etc){body=cleanWhiteSpace(body);var funcText=["var TrimPath_Template_TEMP = function(_OUT, _CONTEXT, _FLAGS) { with (_CONTEXT) {"];var state={stack:[],line:1};var endStmtPrev=-1;while(endStmtPrev+1<body.length){var begStmt=endStmtPrev;begStmt=body.indexOf("{",begStmt+1);while(begStmt>=0){var endStmt=body.indexOf('}',begStmt+1);var stmt=body.substring(begStmt,endStmt);var blockrx=stmt.match(/^\{(cdata|minify|eval)/);if(blockrx){var blockType=blockrx[1];var blockMarkerBeg=begStmt+blockType.length+1;var blockMarkerEnd=body.indexOf('}',blockMarkerBeg);if(blockMarkerEnd>=0){var blockMarker;if(blockMarkerEnd-blockMarkerBeg<=0){blockMarker="{/"+blockType+"}"}else{blockMarker=body.substring(blockMarkerBeg+1,blockMarkerEnd)};var blockEnd=body.indexOf(blockMarker,blockMarkerEnd+1);if(blockEnd>=0){emitSectionText(body.substring(endStmtPrev+1,begStmt),funcText);var blockText=body.substring(blockMarkerEnd+1,blockEnd);if(blockType=='cdata'){emitText(blockText,funcText)}else if(blockType=='minify'){emitText(scrubWhiteSpace(blockText),funcText)}else if(blockType=='eval'){if(blockText!=null&&blockText.length>0)funcText.push('_OUT.write( (function() { '+blockText+' })() );')};begStmt=endStmtPrev=blockEnd+blockMarker.length-1}}}else if(body.charAt(begStmt-1)!='$'&&body.charAt(begStmt-1)!='\\'){var offset=(body.charAt(begStmt+1)=='/'?2:1);if(body.substring(begStmt+offset,begStmt+10+offset).search(TrimPath.parseTemplate_etc.statementTag)==0)break;};begStmt=body.indexOf("{",begStmt+1)};if(begStmt<0)break;var endStmt=body.indexOf("}",begStmt+1);if(endStmt<0)break;emitSectionText(body.substring(endStmtPrev+1,begStmt),funcText);emitStatement(body.substring(begStmt,endStmt+1),state,funcText,tmplName,etc);endStmtPrev=endStmt};emitSectionText(body.substring(endStmtPrev+1),funcText);if(state.stack.length!=0)throw new etc.ParseError(tmplName,state.line,"unclosed, unmatched statement(s): "+state.stack.join(","));funcText.push("}}; TrimPath_Template_TEMP");return funcText.join("")};var emitStatement=function(stmtStr,state,funcText,tmplName,etc){var parts=stmtStr.slice(1,-1).split(' ');var stmt=etc.statementDef[parts[0]];if(stmt==null){emitSectionText(stmtStr,funcText);return};if(stmt.delta<0){if(state.stack.length<=0)throw new etc.ParseError(tmplName,state.line,"close tag does not match any previous statement: "+stmtStr);state.stack.pop()};if(stmt.delta>0)state.stack.push(stmtStr);if(stmt.paramMin!=null&&stmt.paramMin>=parts.length)throw new etc.ParseError(tmplName,state.line,"statement needs more parameters: "+stmtStr);if(stmt.prefixFunc!=null)funcText.push(stmt.prefixFunc(parts,state,tmplName,etc));else funcText.push(stmt.prefix);if(stmt.suffix!=null){if(parts.length<=1){if(stmt.paramDefault!=null)funcText.push(stmt.paramDefault)}else{for(var i=1;i<parts.length;i++){if(i>1)funcText.push(' ');funcText.push(parts[i])}};funcText.push(stmt.suffix)}};var emitSectionText=function(text,funcText){if(text.length<=0)return;var nlPrefix=0;var nlSuffix=text.length-1;while(nlPrefix<text.length&&(text.charAt(nlPrefix)=='\n'))nlPrefix++;while(nlSuffix>=0&&(text.charAt(nlSuffix)==' '||text.charAt(nlSuffix)=='\t'))nlSuffix--;if(nlSuffix<nlPrefix)nlSuffix=nlPrefix;if(nlPrefix>0){funcText.push('if (_FLAGS.keepWhitespace == true) _OUT.write("');var s=text.substring(0,nlPrefix).replace('\n','\\n');if(s.charAt(s.length-1)=='\n')s=s.substring(0,s.length-1);funcText.push(s);funcText.push('");')};var lines=text.substring(nlPrefix,nlSuffix+1).split('\n');for(var i=0;i<lines.length;i++){emitSectionTextLine(lines[i],funcText);if(i<lines.length-1)funcText.push('_OUT.write("\\n");\n')};if(nlSuffix+1<text.length){funcText.push('if (_FLAGS.keepWhitespace == true) _OUT.write("');var s=text.substring(nlSuffix+1).replace('\n','\\n');if(s.charAt(s.length-1)=='\n')s=s.substring(0,s.length-1);funcText.push(s);funcText.push('");')}};var emitSectionTextLine=function(line,funcText){var endMarkPrev='}';var endExprPrev=-1;while(endExprPrev+endMarkPrev.length<line.length){var begMark="${",endMark="}";var begExpr=line.indexOf(begMark,endExprPrev+endMarkPrev.length);if(begExpr<0)break;if(line.charAt(begExpr+2)=='%'){begMark="${%";endMark="%}"};var endExpr=line.indexOf(endMark,begExpr+begMark.length);if(endExpr<0)break;emitText(line.substring(endExprPrev+endMarkPrev.length,begExpr),funcText);var exprArr=line.substring(begExpr+begMark.length,endExpr).replace(/\|\|/g,"#@@#").split('|');for(var k in exprArr){if(exprArr[k].replace)exprArr[k]=exprArr[k].replace(/#@@#/g,'||')};funcText.push('_OUT.write(');emitExpression(exprArr,exprArr.length-1,funcText);funcText.push(');');endExprPrev=endExpr;endMarkPrev=endMark};emitText(line.substring(endExprPrev+endMarkPrev.length),funcText)};var emitText=function(text,funcText){if(text==null||text.length<=0)return;text=text.replace(/\\/g,'\\\\');text=text.replace(/\n/g,'\\n');text=text.replace(/"/g,'\\"');funcText.push('_OUT.write("');funcText.push(text);funcText.push('");')};var emitExpression=function(exprArr,index,funcText){var expr=exprArr[index];if(index<=0){funcText.push(expr);return};var parts=expr.split(':');funcText.push('_MODIFIERS["');funcText.push(parts[0]);funcText.push('"](');emitExpression(exprArr,index-1,funcText);if(parts.length>1){funcText.push(',');funcText.push(parts[1])};funcText.push(')')};var cleanWhiteSpace=function(result){result=result.replace(/\t/g,"    ");result=result.replace(/\r\n/g,"\n");result=result.replace(/\r/g,"\n");result=result.replace(/^(\s*\S*(\s+\S+)*)\s*$/,'$1');return result};var scrubWhiteSpace=function(result){result=result.replace(/^\s+/g,"");result=result.replace(/\s+$/g,"");result=result.replace(/\s+/g," ");result=result.replace(/^(\s*\S*(\s+\S+)*)\s*$/,'$1');return result};TrimPath.parseDOMTemplate=function(elementId,optDocument,optEtc){if(optDocument==null)optDocument=document;var element=optDocument.getElementById(elementId);var content=element.value;if(content==null)content=element.innerHTML;content=content.replace(/&lt;/g,"<").replace(/&gt;/g,">");return TrimPath.parseTemplate(content,elementId,optEtc)};TrimPath.processDOMTemplate=function(elementId,context,optFlags,optDocument,optEtc){return TrimPath.parseDOMTemplate(elementId,optDocument,optEtc).process(context,optFlags)};TrimPath.processTemplate=function(str,context,optFlags,optDocument,optEtc){return TrimPath.parseTemplate(str,optDocument,optEtc).process(context,optFlags)}})();


if (!this.JSON) {
	this.JSON = {};
}
(function() {
	function f(n) {
		return n < 10 ? '0' + n : n;
	}
	if (typeof Date.prototype.toJSON !== 'function') {
		Date.prototype.toJSON = function(key) {
			return isFinite(this.valueOf()) ? this.getUTCFullYear() + '-'
					+ f(this.getUTCMonth() + 1) + '-' + f(this.getUTCDate())
					+ 'T' + f(this.getUTCHours()) + ':'
					+ f(this.getUTCMinutes()) + ':' + f(this.getUTCSeconds())
					+ 'Z' : null;
		};
		String.prototype.toJSON = Number.prototype.toJSON = Boolean.prototype.toJSON = function(
				key) {
			return this.valueOf();
		};
	}
	var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, gap, indent, meta = {
		'\b' : '\\b',
		'\t' : '\\t',
		'\n' : '\\n',
		'\f' : '\\f',
		'\r' : '\\r',
		'"' : '\\"',
		'\\' : '\\\\'
	}, rep;
	function quote(string) {
		escapable.lastIndex = 0;
		return escapable.test(string) ? '"'
				+ string.replace(escapable, function(a) {
					var c = meta[a];
					return typeof c === 'string' ? c : '\\u'
							+ ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
				}) + '"'
				: '"' + string + '"';
	}
	function str(key, holder) {
		var i, k, v, length, mind = gap, partial, value = holder[key];
		if (value && typeof value === 'object'
				&& typeof value.toJSON === 'function') {
			value = value.toJSON(key);
		}
		if (typeof rep === 'function') {
			value = rep.call(holder, key, value);
		}
		switch (typeof value) {
			case 'string' :
				return quote(value);
			case 'number' :
				return isFinite(value) ? String(value) : 'null';
			case 'boolean' :
			case 'null' :
				return String(value);
			case 'object' :
				if (!value) {
					return 'null';
				}
				gap += indent;
				partial = [];
				if (Object.prototype.toString.apply(value) === '[object Array]') {
					length = value.length;
					for (i = 0; i < length; i += 1) {
						partial[i] = str(i, value) || 'null';
					}
					v = partial.length === 0 ? '[]' : gap
							? '[\n' + gap + partial.join(',\n' + gap) + '\n'
									+ mind + ']'
							: '[' + partial.join(',') + ']';
					gap = mind;
					return v;
				}
				if (rep && typeof rep === 'object') {
					length = rep.length;
					for (i = 0; i < length; i += 1) {
						k = rep[i];
						if (typeof k === 'string') {
							v = str(k, value);
							if (v) {
								partial.push(quote(k) + (gap ? ': ' : ':') + v);
							}
						}
					}
				} else {
					for (k in value) {
						if (Object.hasOwnProperty.call(value, k)) {
							v = str(k, value);
							if (v) {
								partial.push(quote(k) + (gap ? ': ' : ':') + v);
							}
						}
					}
				}
				v = partial.length === 0 ? '{}' : gap ? '{\n' + gap
						+ partial.join(',\n' + gap) + '\n' + mind + '}' : '{'
						+ partial.join(',') + '}';
				gap = mind;
				return v;
		}
	}
	if (typeof JSON.stringify !== 'function') {
		JSON.stringify = function(value, replacer, space) {
			var i;
			gap = '';
			indent = '';
			if (typeof space === 'number') {
				for (i = 0; i < space; i += 1) {
					indent += ' ';
				}
			} else if (typeof space === 'string') {
				indent = space;
			}
			rep = replacer;
			if (replacer
					&& typeof replacer !== 'function'
					&& (typeof replacer !== 'object' || typeof replacer.length !== 'number')) {
				throw new Error('JSON.stringify');
			}
			return str('', {
						'' : value
					});
		};
	}
	if (typeof JSON.parse !== 'function') {
		JSON.parse = function(text, reviver) {
			var j;
			function walk(holder, key) {
				var k, v, value = holder[key];
				if (value && typeof value === 'object') {
					for (k in value) {
						if (Object.hasOwnProperty.call(value, k)) {
							v = walk(value, k);
							if (v !== undefined) {
								value[k] = v;
							} else {
								delete value[k];
							}
						}
					}
				}
				return reviver.call(holder, key, value);
			}
			text = String(text);
			cx.lastIndex = 0;
			if (cx.test(text)) {
				text = text.replace(cx, function(a) {
					return '\\u'
							+ ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
				});
			}
			if (/^[\],:{}\s]*$/
					.test(text
							.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@')
							.replace(
									/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
									']').replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
				j = eval('(' + text + ')');
				return typeof reviver === 'function' ? walk({
							'' : j
						}, '') : j;
			}
			throw new SyntaxError('JSON.parse');
		};
	}
}());
/**
 * MD5加密 使用方法 $(document).MD5(str);
 */
(function($) {
	$.fn.MD5 = function(str) {
		return hex_md5(str)
	};
	var opts = {
		hexcase : 0,
		chrsz : 8
	};
	function hex_md5(s) {
		return binl2hex(core_md5(str2binl(s), s.length * opts.chrsz))
	};
	function core_md5(x, len) {
		x[len >> 5] |= 0x80 << ((len) % 32);
		x[(((len + 64) >>> 9) << 4) + 14] = len;
		var a = 1732584193;
		var b = -271733879;
		var c = -1732584194;
		var d = 271733878;
		for (var i = 0; i < x.length; i += 16) {
			var olda = a;
			var oldb = b;
			var oldc = c;
			var oldd = d;
			a = md5_ff(a, b, c, d, x[i + 0], 7, -680876936);
			d = md5_ff(d, a, b, c, x[i + 1], 12, -389564586);
			c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819);
			b = md5_ff(b, c, d, a, x[i + 3], 22, -1044525330);
			a = md5_ff(a, b, c, d, x[i + 4], 7, -176418897);
			d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426);
			c = md5_ff(c, d, a, b, x[i + 6], 17, -1473231341);
			b = md5_ff(b, c, d, a, x[i + 7], 22, -45705983);
			a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416);
			d = md5_ff(d, a, b, c, x[i + 9], 12, -1958414417);
			c = md5_ff(c, d, a, b, x[i + 10], 17, -42063);
			b = md5_ff(b, c, d, a, x[i + 11], 22, -1990404162);
			a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682);
			d = md5_ff(d, a, b, c, x[i + 13], 12, -40341101);
			c = md5_ff(c, d, a, b, x[i + 14], 17, -1502002290);
			b = md5_ff(b, c, d, a, x[i + 15], 22, 1236535329);
			a = md5_gg(a, b, c, d, x[i + 1], 5, -165796510);
			d = md5_gg(d, a, b, c, x[i + 6], 9, -1069501632);
			c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713);
			b = md5_gg(b, c, d, a, x[i + 0], 20, -373897302);
			a = md5_gg(a, b, c, d, x[i + 5], 5, -701558691);
			d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083);
			c = md5_gg(c, d, a, b, x[i + 15], 14, -660478335);
			b = md5_gg(b, c, d, a, x[i + 4], 20, -405537848);
			a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438);
			d = md5_gg(d, a, b, c, x[i + 14], 9, -1019803690);
			c = md5_gg(c, d, a, b, x[i + 3], 14, -187363961);
			b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501);
			a = md5_gg(a, b, c, d, x[i + 13], 5, -1444681467);
			d = md5_gg(d, a, b, c, x[i + 2], 9, -51403784);
			c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473);
			b = md5_gg(b, c, d, a, x[i + 12], 20, -1926607734);
			a = md5_hh(a, b, c, d, x[i + 5], 4, -378558);
			d = md5_hh(d, a, b, c, x[i + 8], 11, -2022574463);
			c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562);
			b = md5_hh(b, c, d, a, x[i + 14], 23, -35309556);
			a = md5_hh(a, b, c, d, x[i + 1], 4, -1530992060);
			d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353);
			c = md5_hh(c, d, a, b, x[i + 7], 16, -155497632);
			b = md5_hh(b, c, d, a, x[i + 10], 23, -1094730640);
			a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174);
			d = md5_hh(d, a, b, c, x[i + 0], 11, -358537222);
			c = md5_hh(c, d, a, b, x[i + 3], 16, -722521979);
			b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189);
			a = md5_hh(a, b, c, d, x[i + 9], 4, -640364487);
			d = md5_hh(d, a, b, c, x[i + 12], 11, -421815835);
			c = md5_hh(c, d, a, b, x[i + 15], 16, 530742520);
			b = md5_hh(b, c, d, a, x[i + 2], 23, -995338651);
			a = md5_ii(a, b, c, d, x[i + 0], 6, -198630844);
			d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415);
			c = md5_ii(c, d, a, b, x[i + 14], 15, -1416354905);
			b = md5_ii(b, c, d, a, x[i + 5], 21, -57434055);
			a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571);
			d = md5_ii(d, a, b, c, x[i + 3], 10, -1894986606);
			c = md5_ii(c, d, a, b, x[i + 10], 15, -1051523);
			b = md5_ii(b, c, d, a, x[i + 1], 21, -2054922799);
			a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359);
			d = md5_ii(d, a, b, c, x[i + 15], 10, -30611744);
			c = md5_ii(c, d, a, b, x[i + 6], 15, -1560198380);
			b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649);
			a = md5_ii(a, b, c, d, x[i + 4], 6, -145523070);
			d = md5_ii(d, a, b, c, x[i + 11], 10, -1120210379);
			c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259);
			b = md5_ii(b, c, d, a, x[i + 9], 21, -343485551);
			a = safe_add(a, olda);
			b = safe_add(b, oldb);
			c = safe_add(c, oldc);
			d = safe_add(d, oldd)
		};
		return Array(a, b, c, d)
	};
	function md5_cmn(q, a, b, x, s, t) {
		return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b)
	};
	function md5_ff(a, b, c, d, x, s, t) {
		return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t)
	};
	function md5_gg(a, b, c, d, x, s, t) {
		return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t)
	};
	function md5_hh(a, b, c, d, x, s, t) {
		return md5_cmn(b ^ c ^ d, a, b, x, s, t)
	};
	function md5_ii(a, b, c, d, x, s, t) {
		return md5_cmn(c ^ (b | (~d)), a, b, x, s, t)
	};
	function safe_add(x, y) {
		var lsw = (x & 0xFFFF) + (y & 0xFFFF);
		var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
		return (msw << 16) | (lsw & 0xFFFF)
	};
	function bit_rol(num, cnt) {
		return (num << cnt) | (num >>> (32 - cnt))
	};
	function str2binl(str) {
		var bin = Array();
		var mask = (1 << opts.chrsz) - 1;
		for (var i = 0; i < str.length * opts.chrsz; i += opts.chrsz)
			bin[i >> 5] |= (str.charCodeAt(i / opts.chrsz) & mask) << (i % 32);
		return bin
	};
	function binl2hex(binarray) {
		var hex_tab = opts.hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
		var str = "";
		for (var i = 0; i < binarray.length * 4; i++) {
			str += hex_tab
					.charAt((binarray[i >> 2] >> ((i % 4) * 8 + 4)) & 0xF)
					+ hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8)) & 0xF)
		};
		return str
	}
})(jQuery)

/*
 * jQurey扩展函数,action,类似于$.ajax;
 * 
 */
jQuery.extend({
	actionAuth : '',
	actionComplete :{},
	actionCompleteTag : {},
	action : function(option) {
		// 默认配置
		var defopt = {
			callback : function() {
			},
			Action : {
			}
		}
		var opt = $.extend({}, defopt, option);
		opt.Action = $.extend({}, defopt.Action, opt.Action);
		// 签名
		$.actionAuth = opt.Action.auth = $(document).MD5($
				.json2str(opt.Action));
		$.actionComplete[$.actionAuth] = opt.callback;
		android.onAction($.json2str(opt.Action));
	},
	actionCallback : function(data) {
		console.log(data)
		data = $.parseJSON(data);
		$.actionComplete[data.auth](data.data);
		$.actionCompleteTag[data.auth] = true;
	},

	/**
	 * 将json转换为字符串 使用方法 json2str({'a':'b'});
	 */
	json2str : function(o) {
		return JSON.stringify(o);
	}
});

/**
 * request 对象,处理请求串. var url = oprequest('/ddd/abd.html?a=aaa');
 * alert(url.getParam('a')) url.setScript("fff.html"); alert(url.getUrl(true));
 */
(function(E) {
	rq = function(url) {
		if ((typeof url) == 'string') {
			this.location = url
		} else {
			this.location = window.location.href
		};
		this.parse()
	};
	rq.prototype = {
//		解码
		parse : function() {
			this.params = {};
			var poswen = this.location.indexOf('?');
			if (poswen > -1) {
				var scriptfile = this.location.substr(0, poswen);
				var params = this.location.substr(poswen + 1).split('&');
				for (var i = 0; i < params.length; i++) {
					var p = params[i];
					var pp = p.split("=");
					this.params[pp[0]] = decodeURIComponent(pp[1]);
				}
			} else {
				var scriptfile = this.location
			};
			this.script = scriptfile.replace(/^[^:]*:\/\/[^\/]*/gi, "");
			this.domain = scriptfile.replace(this.script, "")
		},
		getParams : function() {
			return this.params
		},
		getParam : function(p) {
			if (this.params[p]) {
				return this.params[p]
			} else {
				return false
			}
		},
//		编码
		getParamsStr : function(encode) {
			var params = "";
			var pre = "";
			for (p in this.params) {
				params += pre
						+ p
						+ "="
						+ (true
								? encodeURIComponent(this.params[p])
								: this.params[p]);
				pre = '&'
			};
			return params
		},
		setParam : function(p, v) {
			if (typeof(p) == 'string') {
				this.params[p] = v
			} else {
				this.params[p] = typeof(v)
			};
			return this
		},
		deleteParam : function(name) {
			delete this.params[name];
			return this
		},
		emptyParam : function() {
			this.params = {};
			return this
		},
		setParams : function(params) {
			this.params = $.extend({}, this.params, params);
			return this
		},
		getFileName:function(){
			return this.script.replace(/[^\/]*\//gi,'');
		},
		setFileName:function(v){
			this.script = this.script.replace(/[^\/]+$/,'')+v;
			return this;
		},
		getScript : function() {
			return this.script
		},
		setScript : function(v) {
			if (typeof(v) == "string") {
				if (v.match(/^\//)) {
					this.script = v
				} else {
					var lasts = this.script.lastIndexOf("/");
					if (lasts > -1) {
						this.script = this.script.substr(0, lasts) + "/" + v
					} else {
						this.script = v
					}
				}
			};
			return this
		},
		getDomain : function() {
			return this.domain
		},
		setDomain : function(v) {
			if (typeof(v) == "string") {
				this.domain = v
			};
			return this
		},
		getUrl : function(encode) {
			return this.domain + this.script + "?" + this.getParamsStr(encode)
		}
	};
	return E.oprequest = function(url) {
		return new rq(url)
	}
})(window);


/**
 * 通用弹出框
 */
$(document).ready(function(){
	(function(E) {
		xalert = function(config) {
			var defopt={
				title:nearmelang.lang_opalert_deftitle,
				oktext:nearmelang.lang_opalert_ok,
				canceltext:nearmelang.lang_opalert_cancel,
				oncancel:function(){},
				onok:function(){},
				onhid:function(){},
				autohid:0,
				mode:opalert.BTN_BOTH
			}
			if(typeof(config)=='string'){
				config={content:config,mode:E.opalert.BTN_OK};
			}
			this.opt = $.extend(true,{},defopt,config);
			this.lastorientation = window.orientation;
			this._show();
		};
		xalert.prototype = {
			_show:function(){
				var that = this;
				this._draw();
				$(document.body).css("overflow","hidden");
				this.div.appendTo($(document.body));
				this.board.css({
					left:function(){return Math.round(($(E).width()-$(this).width())/2)+"px";},
					top:function(){return Math.round(($(E).height()-$(this).height())/2)+"px"}
				});
				this.div.css({"z-index":9001}).fadeTo(300,1);
				$(window).bind('resize',function(){that.repos();});
			},
			repos:function(){
				$('.bg',this.board).removeClass('bg').addClass('bg');
				this.board.css({
					left:function(){return Math.round(($(window).width()-$(this).width())/2)+"px";},
					top:function(){return Math.round(($(window).height()-$(this).height())/2)+"px"}
				});
			},
			hide:function(){this._hide();},
			_hide:function(){
				var that = this;
				$(document.body).css("overflow","auto");
				this.div.fadeTo(300,0,function(){$(this).css({"z-index":-1}).remove();});
				this._onhide();
			},
			_draw:function(){
				this.div=$("<div />").addClass("opalert").css({opacity:0,"z-index":-1,top:$(document.body).scrollTop()+"px"}).height($(E).height());
				this._drawbg();
				this._drawfg();
				this._bindEvent();
			},
			_drawbg:function(){
				this.div.append( $("<div />").addClass("bg") );
			},
			_drawfg:function(){
				this.board=$("<div />").addClass("cornerall shadowsbt board");
				this.board.appendTo(this.div);
				if(this.opt.title!=""){
					this.board.append($("<div />").addClass("cornertop title").text(this.opt.title));
				}
				if(this.opt.content && this.opt.content!=""){
					this.board.append($("<div />").addClass("content").text(this.opt.content));
				}
				if(this.opt.mode!=E.opalert.BTN_NONE){
					this.boardbtn=$("<div />").addClass("btnboard");
					this.boardbtn.appendTo(this.board);
					if( (this.opt.mode&E.opalert.BTN_OK)>>1==1 ){
						this.btnok=$("<div />").addClass("btn left shadowsbt").text(this.opt.oktext);
						this.btnok.appendTo(this.boardbtn);
					}
					if( this.opt.mode&E.opalert.BTN_CANCEL==1 ){
						this.btncancel=$("<div />").addClass("btn right shadowsbt").text(this.opt.canceltext);
						this.btncancel.appendTo(this.boardbtn);
					}else if(this.btnok){
						this.btnok.removeClass("left").addClass("center");
					}
				} else {
					if(this.opt.autohid==0){
						this.opt.autohid=5000;
					}
				}
			},
			_bindEvent:function(){
				var that=this;
				if(this.btnok){
					this.btnok.bind("click",function(e){that._onok(e)});
				}
				if(this.btncancel){
					this.btncancel.bind("click",function(e){that._oncancel(e)});
				}
				if(this.opt.autohid>0){
					setTimeout(function(){that._hide()},this.opt.autohid);
				}
				this.div.bind("touchmove",function(e){e.preventDefault()});
			},
			_onhide:function(){
				this.opt.onhid.call(E);
				E.opalert.lastObj = null;
			},
			_onok:function(e){
				this._hide();
				this.opt.onok.call(E,e);
			},
			_oncancel:function(e){
				this._hide();
				this.opt.oncancel.call(E,e);
			}
		};
		E.opalert = function(config) {
			var newobj = new xalert(config);
			E.opalert.lastObj = newobj;
			return newobj;
		}
		E.opalert.BTN_BOTH=3;
		E.opalert.BTN_OK=2;
		E.opalert.BTN_CANCEL=1;
		E.opalert.BTN_NONE=0;
		return E.opalert;
	})(window);
//	opalert('aaa');
});

/**
 * 图片预加载控件
 */
$(document).ready(function(){
	(function(E) {
		var preimg = function() {
			this.src=[];
		};
		preimg.prototype = {
			add:function(id,src){
				this.src.push({id:id,src:src});
				return this;
			},
			action:function(id,src){
				var that = this;
				$.action({
					callback : function(data) {
						that.onload(data);
					},
					Action : {
						"currentHtml" : oprequest().getFileName(),
						"requestPath" : "get_image",
						"requestParams" : $.json2str({id:id,url:src}),
						"requestMethod" : "get",
						"needCallback" : "true",
						"callbackInterface" : "jQuery.actionCallback",
						"callbackParamsType" : "path",
						"needShowNativeLoading" : "false"
					}
				});
			},
			onload:function(info){
				var that=this;
				var image=document.getElementById(info.id);
				if(image){
					image.src=info.path;
				}
			},
			start:function(){
				while(this.src.length>0){
					var d = this.src.shift();
					this.action(d.id,d.src);
				}
				return this;
			}
		};
		E.preimg = function() {
			return new preimg();
		}
		return E.preimg;
	})(window);
});

jQuery.fn.extend({trim:function(){return this.each(function(){this.value=jQuery.trim(this.value);});}})
/**
 * 补齐字符
 * @param {} pad_lenth
 * @param {} pad_string
 * @param {} pad_type
 * @return {}
 */
String.prototype.str_pad=function( pad_lenth,pad_string,pad_type ){
	if( this.length>=pad_lenth ) {return this;} else {pad_lenth -= this.length;}
	pad_string=typeof(pad_string)!="string"?"0":pad_string[0];
	var pads = "";
	while(pads.length<pad_lenth){
		pads+=pad_string;
	}
	switch( pad_type ) {
		case "R":
			return this.toString()+pads;
			break;
		case "L":
		default:
			return pads+this.toString();
	}
}

/**
 * 获取字符长度
 * @return {}
 */
String.prototype.chlength=function(){
	var str = this.toString();
	var len = str.length;
	return Math.ceil((len+str.replace(/[^\u4e00-\u9fa5]/gi,'').length)/2);
}

String.prototype.charlength=function() {
	var str = this.toString();
	var len = str.length;
	return Math.ceil((len+str.replace(/[^\u4e00-\u9fa5]/gi,'').length));
}

/**
 * 获取当前月份的天数
 * @return {}
 */
Date.prototype.getMonthDays=function(){
	var d = new Date(this.getFullYear(),this.getMonth()+1);
	var dt = new Date(this.getFullYear(),this.getMonth() );
	return ( d.getTime() - dt.getTime() ) / 86400000;
}
/**
 * 获取当前时间毫秒数
 * @return {}
 */
if(!Date.now){Date.now=function(){return (new Date()).getTime()};}

/**
 * 自定义长按事件--lclick
 * @example $('#clicker').lclick({onlclick:function(){},time:500});
 * @return {}
 */
(function(A){var B=function(F,E){var D="ontouchstart" in window?"touchstart":"mousedown";var C="ontouchend" in window?"touchend":"mouseup";var G="ontouchmove" in window?"touchmove":"mousemove";F.bind(D,function(){F.data("sttime",Date.now());var H=setInterval(function(){if(Date.now()>=F.data("sttime")+E.time){E.onlclick();clearInterval(F.data("intveral"))}},50);F.data("intveral",H)});F.bind(C,function(){clearInterval(F.data("intveral"))});F.bind(G,function(){clearInterval(F.data("intveral"))});A(document).bind(G,function(I){var H=I.target?I.target:I.srcElement;if(H!=F.get(0)){clearInterval(F.data("intveral"))}})};A.fn.lclick=function(C){var D=A.extend({},A.fn.lclick.defaults,C);return this.each(function(){var E=new B(A(this),D)})};A.fn.lclick.defaults={onlclick:function(){opalert("Triggered")},time:500}})(jQuery);