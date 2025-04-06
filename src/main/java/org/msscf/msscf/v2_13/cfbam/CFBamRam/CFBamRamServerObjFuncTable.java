
// Description: Java 11 in-memory RAM DbIO implementation for ServerObjFunc.

/*
 *	org.msscf.msscf.CFBam
 *
 *	Copyright (c) 2020 Mark Stephen Sobkow
 *	
 *	MSS Code Factory CFBam 2.13 Business Application Model
 *	
 *	Copyright 2020-2021 Mark Stephen Sobkow
 *	
 *		This file is part of MSS Code Factory.
 *	
 *		MSS Code Factory is available under dual commercial license from Mark Stephen
 *		Sobkow, or under the terms of the GNU General Public License, Version 3
 *		or later.
 *	
 *	    MSS Code Factory is free software: you can redistribute it and/or modify
 *	    it under the terms of the GNU General Public License as published by
 *	    the Free Software Foundation, either version 3 of the License, or
 *	    (at your option) any later version.
 *	
 *	    MSS Code Factory is distributed in the hope that it will be useful,
 *	    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	    GNU General Public License for more details.
 *	
 *	    You should have received a copy of the GNU General Public License
 *	    along with MSS Code Factory.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	Donations to support MSS Code Factory can be made at
 *	https://www.paypal.com/paypalme2/MarkSobkow
 *	
 *	Please contact Mark Stephen Sobkow at mark.sobkow@gmail.com for commercial licensing.
 *
 *	Manufactured by MSS Code Factory 2.12
 */

package org.msscf.msscf.v2_13.cfbam.CFBamRam;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.msscf.msscf.v2_13.cflib.CFLib.*;
import org.msscf.msscf.v2_13.cfsec.CFSec.*;
import org.msscf.msscf.v2_13.cfint.CFInt.*;
import org.msscf.msscf.v2_13.cfbam.CFBam.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;
import org.msscf.msscf.v2_13.cfsec.CFSecObj.*;
import org.msscf.msscf.v2_13.cfint.CFIntObj.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamRamServerObjFuncTable in-memory RAM DbIO implementation
 *	for ServerObjFunc.
 */
public class CFBamRamServerObjFuncTable
	implements ICFBamServerObjFuncTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamServerObjFuncBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamServerObjFuncBuff >();
	private Map< CFBamServerObjFuncByRetTblIdxKey,
				Map< CFBamScopePKey,
					CFBamServerObjFuncBuff >> dictByRetTblIdx
		= new HashMap< CFBamServerObjFuncByRetTblIdxKey,
				Map< CFBamScopePKey,
					CFBamServerObjFuncBuff >>();

	public CFBamRamServerObjFuncTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createServerObjFunc( CFSecAuthorization Authorization,
		CFBamServerObjFuncBuff Buff )
	{
		final String S_ProcName = "createServerObjFunc";
		schema.getTableServerMethod().createServerMethod( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerObjFuncByRetTblIdxKey keyRetTblIdx = schema.getFactoryServerObjFunc().newRetTblIdxKey();
		keyRetTblIdx.setOptionalRetTenantId( Buff.getOptionalRetTenantId() );
		keyRetTblIdx.setOptionalRetTableId( Buff.getOptionalRetTableId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"ServerMethod",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamServerObjFuncBuff > subdictRetTblIdx;
		if( dictByRetTblIdx.containsKey( keyRetTblIdx ) ) {
			subdictRetTblIdx = dictByRetTblIdx.get( keyRetTblIdx );
		}
		else {
			subdictRetTblIdx = new HashMap< CFBamScopePKey, CFBamServerObjFuncBuff >();
			dictByRetTblIdx.put( keyRetTblIdx, subdictRetTblIdx );
		}
		subdictRetTblIdx.put( pkey, Buff );

	}

	public CFBamServerObjFuncBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamServerObjFuncBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerObjFuncBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamServerObjFuncBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerObjFuncBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerObjFunc.readAllDerived";
		CFBamServerObjFuncBuff[] retList = new CFBamServerObjFuncBuff[ dictByPKey.values().size() ];
		Iterator< CFBamServerObjFuncBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamServerObjFuncBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		CFBamScopeBuff buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamScopeBuff buff;
			ArrayList<CFBamServerObjFuncBuff> filteredList = new ArrayList<CFBamServerObjFuncBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerObjFuncBuff ) ) {
					filteredList.add( (CFBamServerObjFuncBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerObjFuncBuff[0] ) );
		}
	}

	public CFBamServerObjFuncBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByUNameIdx";
		CFBamServerMethodBuff buff = schema.getTableServerMethod().readDerivedByUNameIdx( Authorization,
			TenantId,
			TableId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof CFBamServerObjFuncBuff ) {
			return( (CFBamServerObjFuncBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerObjFuncBuff[] readDerivedByMethTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByMethTableIdx";
		CFBamServerMethodBuff buffList[] = schema.getTableServerMethod().readDerivedByMethTableIdx( Authorization,
			TenantId,
			TableId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamServerMethodBuff buff;
			ArrayList<CFBamServerObjFuncBuff> filteredList = new ArrayList<CFBamServerObjFuncBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerObjFuncBuff ) ) {
					filteredList.add( (CFBamServerObjFuncBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerObjFuncBuff[0] ) );
		}
	}

	public CFBamServerObjFuncBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByDefSchemaIdx";
		CFBamServerMethodBuff buffList[] = schema.getTableServerMethod().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamServerMethodBuff buff;
			ArrayList<CFBamServerObjFuncBuff> filteredList = new ArrayList<CFBamServerObjFuncBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerObjFuncBuff ) ) {
					filteredList.add( (CFBamServerObjFuncBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerObjFuncBuff[0] ) );
		}
	}

	public CFBamServerObjFuncBuff[] readDerivedByRetTblIdx( CFSecAuthorization Authorization,
		Long RetTenantId,
		Long RetTableId )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readDerivedByRetTblIdx";
		CFBamServerObjFuncByRetTblIdxKey key = schema.getFactoryServerObjFunc().newRetTblIdxKey();
		key.setOptionalRetTenantId( RetTenantId );
		key.setOptionalRetTableId( RetTableId );

		CFBamServerObjFuncBuff[] recArray;
		if( dictByRetTblIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamServerObjFuncBuff > subdictRetTblIdx
				= dictByRetTblIdx.get( key );
			recArray = new CFBamServerObjFuncBuff[ subdictRetTblIdx.size() ];
			Iterator< CFBamServerObjFuncBuff > iter = subdictRetTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamServerObjFuncBuff > subdictRetTblIdx
				= new HashMap< CFBamScopePKey, CFBamServerObjFuncBuff >();
			dictByRetTblIdx.put( key, subdictRetTblIdx );
			recArray = new CFBamServerObjFuncBuff[0];
		}
		return( recArray );
	}

	public CFBamServerObjFuncBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamServerObjFuncBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerObjFuncBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readBuff";
		CFBamServerObjFuncBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a806" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerObjFuncBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamServerObjFuncBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a806" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerObjFuncBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readAllBuff";
		CFBamServerObjFuncBuff buff;
		ArrayList<CFBamServerObjFuncBuff> filteredList = new ArrayList<CFBamServerObjFuncBuff>();
		CFBamServerObjFuncBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a806" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamServerObjFuncBuff[0] ) );
	}

	public CFBamServerObjFuncBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamServerObjFuncBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamServerObjFuncBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerObjFuncBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamServerObjFuncBuff buff;
		ArrayList<CFBamServerObjFuncBuff> filteredList = new ArrayList<CFBamServerObjFuncBuff>();
		CFBamServerObjFuncBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamServerObjFuncBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerObjFuncBuff[0] ) );
	}

	public CFBamServerObjFuncBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByUNameIdx() ";
		CFBamServerObjFuncBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
			return( (CFBamServerObjFuncBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerObjFuncBuff[] readBuffByMethTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByMethTableIdx() ";
		CFBamServerObjFuncBuff buff;
		ArrayList<CFBamServerObjFuncBuff> filteredList = new ArrayList<CFBamServerObjFuncBuff>();
		CFBamServerObjFuncBuff[] buffList = readDerivedByMethTableIdx( Authorization,
			TenantId,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (CFBamServerObjFuncBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerObjFuncBuff[0] ) );
	}

	public CFBamServerObjFuncBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByDefSchemaIdx() ";
		CFBamServerObjFuncBuff buff;
		ArrayList<CFBamServerObjFuncBuff> filteredList = new ArrayList<CFBamServerObjFuncBuff>();
		CFBamServerObjFuncBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (CFBamServerObjFuncBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerObjFuncBuff[0] ) );
	}

	public CFBamServerObjFuncBuff[] readBuffByRetTblIdx( CFSecAuthorization Authorization,
		Long RetTenantId,
		Long RetTableId )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readBuffByRetTblIdx() ";
		CFBamServerObjFuncBuff buff;
		ArrayList<CFBamServerObjFuncBuff> filteredList = new ArrayList<CFBamServerObjFuncBuff>();
		CFBamServerObjFuncBuff[] buffList = readDerivedByRetTblIdx( Authorization,
			RetTenantId,
			RetTableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a806" ) ) {
				filteredList.add( (CFBamServerObjFuncBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerObjFuncBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific ServerObjFunc buffer instances identified by the duplicate key MethTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argTenantId	The ServerObjFunc key attribute of the instance generating the id.
	 *
	 *	@param	argTableId	The ServerObjFunc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamServerObjFuncBuff[] pageBuffByMethTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByMethTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ServerObjFunc buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDefSchemaTenantId	The ServerObjFunc key attribute of the instance generating the id.
	 *
	 *	@param	argDefSchemaId	The ServerObjFunc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamServerObjFuncBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ServerObjFunc buffer instances identified by the duplicate key RetTblIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argRetTenantId	The ServerObjFunc key attribute of the instance generating the id.
	 *
	 *	@param	argRetTableId	The ServerObjFunc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamServerObjFuncBuff[] pageBuffByRetTblIdx( CFSecAuthorization Authorization,
		Long RetTenantId,
		Long RetTableId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByRetTblIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateServerObjFunc( CFSecAuthorization Authorization,
		CFBamServerObjFuncBuff Buff )
	{
		schema.getTableServerMethod().updateServerMethod( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerObjFuncBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateServerObjFunc",
				"Existing record not found",
				"ServerObjFunc",
				pkey );
		}
		CFBamServerObjFuncByRetTblIdxKey existingKeyRetTblIdx = schema.getFactoryServerObjFunc().newRetTblIdxKey();
		existingKeyRetTblIdx.setOptionalRetTenantId( existing.getOptionalRetTenantId() );
		existingKeyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		CFBamServerObjFuncByRetTblIdxKey newKeyRetTblIdx = schema.getFactoryServerObjFunc().newRetTblIdxKey();
		newKeyRetTblIdx.setOptionalRetTenantId( Buff.getOptionalRetTenantId() );
		newKeyRetTblIdx.setOptionalRetTableId( Buff.getOptionalRetTableId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateServerObjFunc",
						"Superclass",
						"SuperClass",
						"ServerMethod",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamServerObjFuncBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByRetTblIdx.get( existingKeyRetTblIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRetTblIdx.containsKey( newKeyRetTblIdx ) ) {
			subdict = dictByRetTblIdx.get( newKeyRetTblIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamServerObjFuncBuff >();
			dictByRetTblIdx.put( newKeyRetTblIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteServerObjFunc( CFSecAuthorization Authorization,
		CFBamServerObjFuncBuff Buff )
	{
		final String S_ProcName = "CFBamRamServerObjFuncTable.deleteServerObjFunc() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerObjFuncBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteServerObjFunc",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckParams[] = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		if( arrCheckParams.length > 0 ) {
			schema.getTableParam().deleteParamByServerMethodIdx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		}
		CFBamServerObjFuncByRetTblIdxKey keyRetTblIdx = schema.getFactoryServerObjFunc().newRetTblIdxKey();
		keyRetTblIdx.setOptionalRetTenantId( existing.getOptionalRetTenantId() );
		keyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamServerObjFuncBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByRetTblIdx.get( keyRetTblIdx );
		subdict.remove( pkey );

		schema.getTableServerMethod().deleteServerMethod( Authorization,
			Buff );
	}
	public void deleteServerObjFuncByRetTblIdx( CFSecAuthorization Authorization,
		Long argRetTenantId,
		Long argRetTableId )
	{
		CFBamServerObjFuncByRetTblIdxKey key = schema.getFactoryServerObjFunc().newRetTblIdxKey();
		key.setOptionalRetTenantId( argRetTenantId );
		key.setOptionalRetTableId( argRetTableId );
		deleteServerObjFuncByRetTblIdx( Authorization, key );
	}

	public void deleteServerObjFuncByRetTblIdx( CFSecAuthorization Authorization,
		CFBamServerObjFuncByRetTblIdxKey argKey )
	{
		CFBamServerObjFuncBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalRetTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalRetTableId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerObjFuncBuff> matchSet = new LinkedList<CFBamServerObjFuncBuff>();
		Iterator<CFBamServerObjFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerObjFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argTableId,
		String argName )
	{
		CFBamServerMethodByUNameIdxKey key = schema.getFactoryServerMethod().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerObjFuncByUNameIdx( Authorization, key );
	}

	public void deleteServerObjFuncByUNameIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByUNameIdxKey argKey )
	{
		CFBamServerObjFuncBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerObjFuncBuff> matchSet = new LinkedList<CFBamServerObjFuncBuff>();
		Iterator<CFBamServerObjFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerObjFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByMethTableIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argTableId )
	{
		CFBamServerMethodByMethTableIdxKey key = schema.getFactoryServerMethod().newMethTableIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredTableId( argTableId );
		deleteServerObjFuncByMethTableIdx( Authorization, key );
	}

	public void deleteServerObjFuncByMethTableIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByMethTableIdxKey argKey )
	{
		CFBamServerObjFuncBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerObjFuncBuff> matchSet = new LinkedList<CFBamServerObjFuncBuff>();
		Iterator<CFBamServerObjFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerObjFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamServerMethodByDefSchemaIdxKey key = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerObjFuncByDefSchemaIdx( Authorization, key );
	}

	public void deleteServerObjFuncByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByDefSchemaIdxKey argKey )
	{
		CFBamServerObjFuncBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerObjFuncBuff> matchSet = new LinkedList<CFBamServerObjFuncBuff>();
		Iterator<CFBamServerObjFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerObjFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteServerObjFuncByIdIdx( Authorization, key );
	}

	public void deleteServerObjFuncByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamServerObjFuncBuff cur;
		LinkedList<CFBamServerObjFuncBuff> matchSet = new LinkedList<CFBamServerObjFuncBuff>();
		Iterator<CFBamServerObjFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerObjFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerObjFuncByTenantIdx( Authorization, key );
	}

	public void deleteServerObjFuncByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamServerObjFuncBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerObjFuncBuff> matchSet = new LinkedList<CFBamServerObjFuncBuff>();
		Iterator<CFBamServerObjFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerObjFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
