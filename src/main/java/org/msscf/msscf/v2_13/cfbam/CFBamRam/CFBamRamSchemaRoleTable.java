
// Description: Java 11 in-memory RAM DbIO implementation for SchemaRole.

/*
 *	org.msscf.msscf.CFBam
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal CFBam 2.13 Business Application Model
 *	
 *	Copyright 2016-2026 Mark Stephen Sobkow
 *	
 *	This file is part of Mark's Code Fractal CFBam.
 *	
 *	Mark's Code Fractal CFBam is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU General Public License,
 *	Version 3 or later with classpath and static linking exceptions.
 *	
 *	As a special exception, Mark Sobkow gives you permission to link this library
 *	with independent modules to produce an executable, provided that none of them
 *	conflict with the intent of the GPLv3; that is, you are not allowed to invoke
 *	the methods of this library from non-GPLv3-compatibly licensed code. You may not
 *	implement an LPGLv3 "wedge" to try to bypass this restriction. That said, code which
 *	does not rely on this library is free to specify whatever license its authors decide
 *	to use. Mark Sobkow specifically rejects the infectious nature of the GPLv3, and
 *	considers the mere act of including GPLv3 modules in an executable to be perfectly
 *	reasonable given tools like modern Java's single-jar deployment options.
 *	
 *	Mark's Code Fractal CFBam is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	Mark's Code Fractal CFBam is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with Mark's Code Fractal CFBam.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	If you wish to modify and use this code without publishing your changes,
 *	or integrate it with proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
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
import org.msscf.msscf.v2_13.cflib.CFLib.xml.*;
import org.msscf.msscf.v2_13.cfsec.CFSec.*;
import org.msscf.msscf.v2_13.cfint.CFInt.*;
import org.msscf.msscf.v2_13.cfbam.CFBam.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;
import org.msscf.msscf.v2_13.cfsec.CFSecObj.*;
import org.msscf.msscf.v2_13.cfint.CFIntObj.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamRamSchemaRoleTable in-memory RAM DbIO implementation
 *	for SchemaRole.
 */
public class CFBamRamSchemaRoleTable
	implements ICFBamSchemaRoleTable
{
	private ICFBamSchema schema;
	private Map< CFBamRoleDefPKey,
				CFBamSchemaRoleBuff > dictByPKey
		= new HashMap< CFBamRoleDefPKey,
				CFBamSchemaRoleBuff >();
	private Map< CFBamSchemaRoleBySchemaIdxKey,
				Map< CFBamRoleDefPKey,
					CFBamSchemaRoleBuff >> dictBySchemaIdx
		= new HashMap< CFBamSchemaRoleBySchemaIdxKey,
				Map< CFBamRoleDefPKey,
					CFBamSchemaRoleBuff >>();

	public CFBamRamSchemaRoleTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createSchemaRole( CFSecAuthorization Authorization,
		CFBamSchemaRoleBuff Buff )
	{
		final String S_ProcName = "createSchemaRole";
		schema.getTableRoleDef().createRoleDef( Authorization,
			Buff );
		CFBamRoleDefPKey pkey = schema.getFactoryRoleDef().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaRoleBySchemaIdxKey keySchemaIdx = schema.getFactorySchemaRole().newSchemaIdxKey();
		keySchemaIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

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
				if( null == schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"RoleDef",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Schema",
						"SchemaDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamRoleDefPKey, CFBamSchemaRoleBuff > subdictSchemaIdx;
		if( dictBySchemaIdx.containsKey( keySchemaIdx ) ) {
			subdictSchemaIdx = dictBySchemaIdx.get( keySchemaIdx );
		}
		else {
			subdictSchemaIdx = new HashMap< CFBamRoleDefPKey, CFBamSchemaRoleBuff >();
			dictBySchemaIdx.put( keySchemaIdx, subdictSchemaIdx );
		}
		subdictSchemaIdx.put( pkey, Buff );

	}

	public CFBamSchemaRoleBuff readDerived( CFSecAuthorization Authorization,
		CFBamRoleDefPKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readDerived";
		CFBamRoleDefPKey key = schema.getFactoryRoleDef().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaRoleBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRoleBuff lockDerived( CFSecAuthorization Authorization,
		CFBamRoleDefPKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readDerived";
		CFBamRoleDefPKey key = schema.getFactoryRoleDef().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaRoleBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRoleBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamSchemaRole.readAllDerived";
		CFBamSchemaRoleBuff[] retList = new CFBamSchemaRoleBuff[ dictByPKey.values().size() ];
		Iterator< CFBamSchemaRoleBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamSchemaRoleBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByUNameIdx";
		CFBamRoleDefBuff buff = schema.getTableRoleDef().readDerivedByUNameIdx( Authorization,
			TenantId,
			ScopeId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof CFBamSchemaRoleBuff ) {
			return( (CFBamSchemaRoleBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaRoleBuff readDerivedByUDefIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByUDefIdx";
		CFBamRoleDefBuff buff = schema.getTableRoleDef().readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof CFBamSchemaRoleBuff ) {
			return( (CFBamSchemaRoleBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaRoleBuff[] readDerivedByRoleDefTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByRoleDefTentIdx";
		CFBamRoleDefBuff buffList[] = schema.getTableRoleDef().readDerivedByRoleDefTentIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamRoleDefBuff buff;
			ArrayList<CFBamSchemaRoleBuff> filteredList = new ArrayList<CFBamSchemaRoleBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamSchemaRoleBuff ) ) {
					filteredList.add( (CFBamSchemaRoleBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamSchemaRoleBuff[0] ) );
		}
	}

	public CFBamSchemaRoleBuff[] readDerivedByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByScopeIdx";
		CFBamRoleDefBuff buffList[] = schema.getTableRoleDef().readDerivedByScopeIdx( Authorization,
			TenantId,
			ScopeId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamRoleDefBuff buff;
			ArrayList<CFBamSchemaRoleBuff> filteredList = new ArrayList<CFBamSchemaRoleBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamSchemaRoleBuff ) ) {
					filteredList.add( (CFBamSchemaRoleBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamSchemaRoleBuff[0] ) );
		}
	}

	public CFBamSchemaRoleBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByDefSchemaIdx";
		CFBamRoleDefBuff buffList[] = schema.getTableRoleDef().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamRoleDefBuff buff;
			ArrayList<CFBamSchemaRoleBuff> filteredList = new ArrayList<CFBamSchemaRoleBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamSchemaRoleBuff ) ) {
					filteredList.add( (CFBamSchemaRoleBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamSchemaRoleBuff[0] ) );
		}
	}

	public CFBamSchemaRoleBuff[] readDerivedBySchemaIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaDefId )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readDerivedBySchemaIdx";
		CFBamSchemaRoleBySchemaIdxKey key = schema.getFactorySchemaRole().newSchemaIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredSchemaDefId( SchemaDefId );

		CFBamSchemaRoleBuff[] recArray;
		if( dictBySchemaIdx.containsKey( key ) ) {
			Map< CFBamRoleDefPKey, CFBamSchemaRoleBuff > subdictSchemaIdx
				= dictBySchemaIdx.get( key );
			recArray = new CFBamSchemaRoleBuff[ subdictSchemaIdx.size() ];
			Iterator< CFBamSchemaRoleBuff > iter = subdictSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRoleDefPKey, CFBamSchemaRoleBuff > subdictSchemaIdx
				= new HashMap< CFBamRoleDefPKey, CFBamSchemaRoleBuff >();
			dictBySchemaIdx.put( key, subdictSchemaIdx );
			recArray = new CFBamSchemaRoleBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaRoleBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByIdIdx() ";
		CFBamRoleDefPKey key = schema.getFactoryRoleDef().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamSchemaRoleBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRoleBuff readBuff( CFSecAuthorization Authorization,
		CFBamRoleDefPKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readBuff";
		CFBamSchemaRoleBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88e" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRoleBuff lockBuff( CFSecAuthorization Authorization,
		CFBamRoleDefPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamSchemaRoleBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88e" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRoleBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readAllBuff";
		CFBamSchemaRoleBuff buff;
		ArrayList<CFBamSchemaRoleBuff> filteredList = new ArrayList<CFBamSchemaRoleBuff>();
		CFBamSchemaRoleBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88e" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRoleBuff[0] ) );
	}

	public CFBamSchemaRoleBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByIdIdx() ";
		CFBamSchemaRoleBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
			return( (CFBamSchemaRoleBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaRoleBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByUNameIdx() ";
		CFBamSchemaRoleBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			ScopeId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
			return( (CFBamSchemaRoleBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaRoleBuff readBuffByUDefIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByUDefIdx() ";
		CFBamSchemaRoleBuff buff = readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
			return( (CFBamSchemaRoleBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaRoleBuff[] readBuffByRoleDefTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByRoleDefTentIdx() ";
		CFBamSchemaRoleBuff buff;
		ArrayList<CFBamSchemaRoleBuff> filteredList = new ArrayList<CFBamSchemaRoleBuff>();
		CFBamSchemaRoleBuff[] buffList = readDerivedByRoleDefTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
				filteredList.add( (CFBamSchemaRoleBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRoleBuff[0] ) );
	}

	public CFBamSchemaRoleBuff[] readBuffByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByScopeIdx() ";
		CFBamSchemaRoleBuff buff;
		ArrayList<CFBamSchemaRoleBuff> filteredList = new ArrayList<CFBamSchemaRoleBuff>();
		CFBamSchemaRoleBuff[] buffList = readDerivedByScopeIdx( Authorization,
			TenantId,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
				filteredList.add( (CFBamSchemaRoleBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRoleBuff[0] ) );
	}

	public CFBamSchemaRoleBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByDefSchemaIdx() ";
		CFBamSchemaRoleBuff buff;
		ArrayList<CFBamSchemaRoleBuff> filteredList = new ArrayList<CFBamSchemaRoleBuff>();
		CFBamSchemaRoleBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
				filteredList.add( (CFBamSchemaRoleBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRoleBuff[0] ) );
	}

	public CFBamSchemaRoleBuff[] readBuffBySchemaIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaDefId )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readBuffBySchemaIdx() ";
		CFBamSchemaRoleBuff buff;
		ArrayList<CFBamSchemaRoleBuff> filteredList = new ArrayList<CFBamSchemaRoleBuff>();
		CFBamSchemaRoleBuff[] buffList = readDerivedBySchemaIdx( Authorization,
			TenantId,
			SchemaDefId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88e" ) ) {
				filteredList.add( (CFBamSchemaRoleBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRoleBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific SchemaRole buffer instances identified by the duplicate key SchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argTenantId	The SchemaRole key attribute of the instance generating the id.
	 *
	 *	@param	argSchemaDefId	The SchemaRole key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaRoleBuff[] pageBuffBySchemaIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaDefId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffBySchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateSchemaRole( CFSecAuthorization Authorization,
		CFBamSchemaRoleBuff Buff )
	{
		schema.getTableRoleDef().updateRoleDef( Authorization,
			Buff );
		CFBamRoleDefPKey pkey = schema.getFactoryRoleDef().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaRoleBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateSchemaRole",
				"Existing record not found",
				"SchemaRole",
				pkey );
		}
		CFBamSchemaRoleBySchemaIdxKey existingKeySchemaIdx = schema.getFactorySchemaRole().newSchemaIdxKey();
		existingKeySchemaIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamSchemaRoleBySchemaIdxKey newKeySchemaIdx = schema.getFactorySchemaRole().newSchemaIdxKey();
		newKeySchemaIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaRole",
						"Superclass",
						"SuperClass",
						"RoleDef",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaRole",
						"Container",
						"Schema",
						"SchemaDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamRoleDefPKey, CFBamSchemaRoleBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictBySchemaIdx.get( existingKeySchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchemaIdx.containsKey( newKeySchemaIdx ) ) {
			subdict = dictBySchemaIdx.get( newKeySchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamRoleDefPKey, CFBamSchemaRoleBuff >();
			dictBySchemaIdx.put( newKeySchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteSchemaRole( CFSecAuthorization Authorization,
		CFBamSchemaRoleBuff Buff )
	{
		final String S_ProcName = "CFBamRamSchemaRoleTable.deleteSchemaRole() ";
		String classCode;
		CFBamRoleDefPKey pkey = schema.getFactoryRoleDef().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaRoleBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteSchemaRole",
				pkey );
		}
		CFBamSchemaRoleBySchemaIdxKey keySchemaIdx = schema.getFactorySchemaRole().newSchemaIdxKey();
		keySchemaIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamRoleDefPKey, CFBamSchemaRoleBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaIdx.get( keySchemaIdx );
		subdict.remove( pkey );

		schema.getTableRoleDef().deleteRoleDef( Authorization,
			Buff );
	}
	public void deleteSchemaRoleBySchemaIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argSchemaDefId )
	{
		CFBamSchemaRoleBySchemaIdxKey key = schema.getFactorySchemaRole().newSchemaIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredSchemaDefId( argSchemaDefId );
		deleteSchemaRoleBySchemaIdx( Authorization, key );
	}

	public void deleteSchemaRoleBySchemaIdx( CFSecAuthorization Authorization,
		CFBamSchemaRoleBySchemaIdxKey argKey )
	{
		CFBamSchemaRoleBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRoleBuff> matchSet = new LinkedList<CFBamSchemaRoleBuff>();
		Iterator<CFBamSchemaRoleBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRoleBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRole( Authorization, cur );
		}
	}

	public void deleteSchemaRoleByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamRoleDefPKey key = schema.getFactoryRoleDef().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteSchemaRoleByIdIdx( Authorization, key );
	}

	public void deleteSchemaRoleByIdIdx( CFSecAuthorization Authorization,
		CFBamRoleDefPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamSchemaRoleBuff cur;
		LinkedList<CFBamSchemaRoleBuff> matchSet = new LinkedList<CFBamSchemaRoleBuff>();
		Iterator<CFBamSchemaRoleBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRoleBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRole( Authorization, cur );
		}
	}

	public void deleteSchemaRoleByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId,
		String argName )
	{
		CFBamRoleDefByUNameIdxKey key = schema.getFactoryRoleDef().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteSchemaRoleByUNameIdx( Authorization, key );
	}

	public void deleteSchemaRoleByUNameIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByUNameIdxKey argKey )
	{
		CFBamSchemaRoleBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRoleBuff> matchSet = new LinkedList<CFBamSchemaRoleBuff>();
		Iterator<CFBamSchemaRoleBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRoleBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRole( Authorization, cur );
		}
	}

	public void deleteSchemaRoleByUDefIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId,
		Long argDefSchemaTenantId,
		Long argDefSchemaId,
		String argName )
	{
		CFBamRoleDefByUDefIdxKey key = schema.getFactoryRoleDef().newUDefIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		key.setRequiredName( argName );
		deleteSchemaRoleByUDefIdx( Authorization, key );
	}

	public void deleteSchemaRoleByUDefIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByUDefIdxKey argKey )
	{
		CFBamSchemaRoleBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( argKey.getOptionalDefSchemaTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRoleBuff> matchSet = new LinkedList<CFBamSchemaRoleBuff>();
		Iterator<CFBamSchemaRoleBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRoleBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRole( Authorization, cur );
		}
	}

	public void deleteSchemaRoleByRoleDefTentIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamRoleDefByRoleDefTentIdxKey key = schema.getFactoryRoleDef().newRoleDefTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteSchemaRoleByRoleDefTentIdx( Authorization, key );
	}

	public void deleteSchemaRoleByRoleDefTentIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByRoleDefTentIdxKey argKey )
	{
		CFBamSchemaRoleBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRoleBuff> matchSet = new LinkedList<CFBamSchemaRoleBuff>();
		Iterator<CFBamSchemaRoleBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRoleBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRole( Authorization, cur );
		}
	}

	public void deleteSchemaRoleByScopeIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId )
	{
		CFBamRoleDefByScopeIdxKey key = schema.getFactoryRoleDef().newScopeIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		deleteSchemaRoleByScopeIdx( Authorization, key );
	}

	public void deleteSchemaRoleByScopeIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByScopeIdxKey argKey )
	{
		CFBamSchemaRoleBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRoleBuff> matchSet = new LinkedList<CFBamSchemaRoleBuff>();
		Iterator<CFBamSchemaRoleBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRoleBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRole( Authorization, cur );
		}
	}

	public void deleteSchemaRoleByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamRoleDefByDefSchemaIdxKey key = schema.getFactoryRoleDef().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteSchemaRoleByDefSchemaIdx( Authorization, key );
	}

	public void deleteSchemaRoleByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByDefSchemaIdxKey argKey )
	{
		CFBamSchemaRoleBuff cur;
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
		LinkedList<CFBamSchemaRoleBuff> matchSet = new LinkedList<CFBamSchemaRoleBuff>();
		Iterator<CFBamSchemaRoleBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRoleBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRole( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
